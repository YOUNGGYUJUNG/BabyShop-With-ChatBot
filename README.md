챗봇 육아용품 사이트
프로젝트 기간: 2025.01 ~ 2025.01
참여 인원: 백엔드 5명

1. 프로젝트 개요
1.1. 프로젝트 배경
육아에 대한 정보가 부족하거나, 육아용품 선물 선택에 어려움을 겪는 분들을 위해 맞춤형 챗봇 서비스를 제공하는 육아용품 사이트입니다. 사용자가 챗봇과 상호작용하며 육아 관련 정보를 얻고, 적합한 육아용품을 추천받을 수 있도록 설계되었습니다.

1.2. 프로젝트 목적
육아용품 공지사항 및 FAQ 제공: 육아용품에 대한 최신 공지사항과 자주 묻는 질문(FAQ)을 체계적으로 제공하여 사용자가 쉽게 정보를 얻을 수 있도록 함.
효율적인 서버 간 통신 구축: Spring Boot와 FastAPI 서버 간의 원활한 통신을 구축하여 서비스 안정성을 확보.
챗봇 서비스 최적화: 3단계 필터링(ElasticSearch, LangChain, LLM)을 통한 답변 최적화로 LLM 모델 사용에 따른 비용 부담을 대폭 절감.
2. 사용 기술
Backend Framework: Spring Boot (Java 기반 웹/애플리케이션 서버)
검색 엔진: ElasticSearch
→ 검색 필터링 및 FAQ 답변 처리에 활용
캐시/메시지 브로커: Redis
→ LangChain이 Redis에서 데이터를 읽어와 반복 질문에 대한 빠른 응답 지원
데이터베이스: MySQL
→ 육아용품 정보, 공지사항, FAQ 등 데이터 저장
챗봇 백엔드: Python, FastAPI
→ 챗봇 서비스 제작 및 API 제공
컨테이너 관리: Docker
→ Redis, ElasticSearch, MySQL 등 멀티 컨테이너 환경 구축
3. 시스템 아키텍처 및 역할
3.1. 시스템 아키텍처
프론트엔드 사용자 인터페이스: 사용자는 웹 또는 모바일을 통해 사이트에 접속하여 챗봇과 상호작용.
Spring Boot 서버: 육아용품 관련 공지사항 및 FAQ 제작과 관리, FastAPI 서버와 통신하여 서비스 연계.
FastAPI 서버: Python 기반으로 챗봇 서비스를 제공하며, LLM 모델(Upstage의 Solar-pro LLM)을 호출하여 질문에 대한 답변을 생성.
데이터 및 캐시 계층:
ElasticSearch: 초기 1차 필터링 단계로 검색 필터링 및 FAQ 답변 제공
Redis: LangChain이 읽어와 반복 질문에 대한 빠른 응답을 제공
MySQL: 육아용품 관련 데이터, 공지사항, FAQ 등의 영구 저장소
Docker 컨테이너: 멀티 컨테이너 환경에서 Redis, ElasticSearch, MySQL 등을 통합 관리하며, 개발 및 배포의 효율성을 극대화.
3.2. 담당 역할
육아용품 공지사항 및 FAQ 제작: 사용자들이 필요한 정보를 빠르게 찾을 수 있도록 데이터 구성 및 관리.
서버 간 통신 연결 구축: Spring Boot 서버와 FastAPI 서버 간의 원활한 통신을 위한 API 인터페이스 설계 및 구현.
Docker 멀티 컨테이너 환경 구성: Docker를 활용해 Redis, ElasticSearch, MySQL 컨테이너를 효율적으로 구성 및 관리.
챗봇 서비스 제작: 단계별 필터링(ElasticSearch → LangChain → LLM)을 통해 최적의 답변을 제공하는 챗봇 서비스 개발.
4. 챗봇 서비스 필터링 단계
4.1. 1차 필터링 - ElasticSearch
역할: 사용자가 입력한 질문에 대해 검색 필터링을 수행하여 FAQ 데이터베이스에서 관련 답변을 탐색.
효과: LLM에 부하를 줄이면서도 자주 묻는 질문에 대한 빠른 응답 제공.
4.2. 2차 필터링 - LangChain
역할: ElasticSearch를 통과한 질문에 대해 Redis에 저장된 데이터를 활용, 반복되는 질문에 대한 빠른 답변을 도출.
효과: 반복 질문에 대한 캐싱 효과와 빠른 응답 시간 확보.
4.3. 3차 필터링 - LLM (Upstage의 Solar-pro LLM)
역할: 1차와 2차 필터링을 거친 질문에 대해 심도 있는 답변 생성.
효과: LLM 모델의 복잡한 연산 부담을 경감하여 성능 최적화 및 비용 절감 효과 달성.
5. 주요 성과
비용 효율적 LLM 사용: 3단계 필터링을 통해 LLM 모델 호출 빈도를 최소화하여 비용 부담을 크게 절감.
높은 성능과 안정성: Spring Boot와 FastAPI 서버 간의 견고한 통신 연결 및 Docker 기반 멀티 컨테이너 환경 구축으로 서비스의 안정성 및 확장성 확보.
사용자 만족도 향상: 체계적인 공지사항 및 FAQ 관리, 그리고 빠르고 정확한 챗봇 응답 제공으로 사용자 경험 개선.
6. 설치 및 실행 방법
6.1. Docker 기반 멀티 컨테이너 구성
Docker 이미지 다운로드 (예시):

bash
복사
# MySQL, Redis, ElasticSearch 컨테이너 실행 예시
docker-compose up -d
docker-compose.yml 예시:

yaml
복사
version: '3'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: chatbot_db
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:latest
    ports:
      - "6379:6379"

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.10.1
    environment:
      - discovery.type=single-node
    ports:
      - "9200:9200"

volumes:
  mysql_data:
서버 실행:

Spring Boot 서버 및 FastAPI 서버는 각각의 프로젝트 디렉토리에서 실행합니다.
Spring Boot: ./mvnw spring-boot:run
FastAPI: uvicorn main:app --reload
6.2. 기타 설정
환경 변수 및 설정 파일: 각 서버의 설정 파일(application.properties, config.yaml 등)을 통해 필요한 환경 변수와 연결 정보를 설정합니다.
API 문서: FastAPI는 기본적으로 /docs 경로에서 Swagger UI를 제공하므로, API 테스트에 유용합니다.
7. 데모 영상
아래는 프로젝트 데모 영상(유튜브 동영상)입니다.


참고: 위의 영상 링크와 썸네일 URL은 실제 유튜브 영상 ID로 교체해 주세요.

