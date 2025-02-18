import json
from fastapi import FastAPI, Request, HTTPException
from elasticsearch import Elasticsearch
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv
import os
import pymysql
from langchain_upstage import ChatUpstage
from langchain_core.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser

load_dotenv()

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Elasticsearch 클라이언트 초기화
es = Elasticsearch(
    hosts = ["https://my-elasticsearch-project-a85a66.es.us-east-1.aws.elastic.cloud:443"],
    api_key="eDBUU2k1UUJkU1c4VW54TXZLcFE6ZU9pUmUzTktUX2E1bUp6azc2RVM2QQ=="
    )

# MySQL 접속 정보를 환경변수(.env)에서 불러오기
MYSQL_HOST = os.getenv("MYSQL_HOST", "localhost")
MYSQL_PORT = int(os.getenv("MYSQL_PORT", "3306"))
MYSQL_USER = os.getenv("MYSQL_USER", "root")
MYSQL_PASSWORD = os.getenv("MYSQL_PASSWORD", "1234")
MYSQL_DB = os.getenv("MYSQL_DB", "fourth_apple_dev")

def get_mysql_connection():
    try:
        connection = pymysql.connect(
            host=MYSQL_HOST,
            port=MYSQL_PORT,
            user=MYSQL_USER,
            password=MYSQL_PASSWORD,
            database=MYSQL_DB,
            charset='utf8mb4',
            cursorclass=pymysql.cursors.DictCursor,
        )
        return connection
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"MySQL 연결 오류: {e}")

# LLM 초기화 (업스테이지)
llm = ChatUpstage(model="solar-pro", api_key="up_uGtLhbLT7ctnF3pldKOR2gxINvm1O")

# 프롬프트 템플릿 수정 (이름, 가격, 설명 포함)
prompt_template = PromptTemplate.from_template("""
당신은 세상에서 가장 친절한 상담원입니다.
고객의 요청을 바탕으로 아래의 상품 목록 중에서 적절한 상품을 추천해 주세요.

상품 목록:
{product_list}

고객의 요청: {question}

반드시 위 목록에 있는 상품만 추천해주시고,
추천하는 이름, 가격, 설명, 사진, 상품 링크을 함께 제공해 주세요.
사진은 <img src= \\" image  \\" style="width:300px; height:200px;"> 으로 넣어주세요.
링크는 값은 <a href="product/<<id>>">상품 보러 가기</a> 으로 넣어주세요.
id는 상품 id를 넣어주세요.
추천할 때 상품 5개 이하로 보여주세요.
그대신 정확한 갯수를 보여달라고 하면 그 갯수만큼 보여달라고 해주세요.
육아 관련 질문은 제외하고 다른 모든 질문은 "해당 질문을 이해하지 못했습니다."만 대답해주세요.
""")


chain = prompt_template | llm | StrOutputParser()

@app.post("/process")
async def process_message(request: Request):
    """
    1. 먼저 Elasticsearch에서 검색합니다.
    2. Elasticsearch 결과가 없으면, DB에서 저장된 상품 목록을 불러옵니다.
    3. 불러온 DB 데이터를 LLM 프롬프트에 포함하여 추천 응답을 생성합니다.
    """
    data = await request.json()
    user_input = data.get("message", "").strip()

    if not user_input:
        raise HTTPException(status_code=400, detail="메시지가 제공되지 않았습니다.")

    # Step 1: Elasticsearch 조회 (인덱스 'faq' 사용)
    query_body = {
        "query": {
            "match": {
                "question": user_input
            }
        }
    }

    try:
        es_result = es.search(index="faq", body=query_body)
        print(es_result)
        total_hits = (
            es_result["hits"]["total"].get("value", 0)
            if isinstance(es_result["hits"]["total"], dict)
            else es_result["hits"]["total"]
        )

        if total_hits > 0:
            first_hit = es_result["hits"]["hits"][0]["_source"]
            answer = first_hit.get("answer", "답변 정보가 존재하지 않습니다.")
            return answer
        else:
            # Step 2: Elasticsearch 결과가 없을 경우, DB에서 상품 목록 조회
            conn = get_mysql_connection()
            try:
                with conn.cursor() as cursor:
                    # 유모차 관련 상품 조회 (이름, 가격, 설명 포함)
                    sql = """
                        SELECT
                            p.product_name, p.price, p.description, p.id, p.image
                        FROM
                            product p

                    """  # 필요에 따라 쿼리 수정
                    cursor.execute(sql)
                    results = cursor.fetchall()
            finally:
                conn.close()

            if results:
                # 상품 목록을 JSON 형태의 문자열로 구성
                product_list = json.dumps(results, ensure_ascii=False)

                # Step 3: DB 데이터를 LLM 프롬프트에 포함하여 추천 요청
                prompt = prompt_template.format(product_list=product_list, question=user_input)
                try:
                    response = chain.invoke({"question": user_input, "product_list": product_list})
                    return response
                except Exception as e:
                    raise HTTPException(status_code=500, detail=f"LangChain 처리 오류: {e}")
            else:
                return "죄송합니다. 현재 추천 가능한 상품 데이터가 없습니다."
                
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"처리 중 오류 발생: {e}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)