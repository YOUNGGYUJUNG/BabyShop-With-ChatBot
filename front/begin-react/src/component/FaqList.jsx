// src/components/QnaList.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/QnaList.css'; // 커스텀 CSS 파일 임포트

function QnaList() {
  const [faqs, setFaqs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 페이지 정보 (예: 0번째 페이지, 한 페이지당 10건)
  const [page, setPage] = useState(0);
  const size = 10;

  useEffect(() => {
    const fetchFaqs = async () => {
      try {
        const url = `http://localhost:8080/faq?page=${page}&size=${size}`;
        const response = await fetch(url);
        if (!response.ok) {
          throw new Error(`서버 오류: ${response.status}`);
        }
        const data = await response.json();
        // 실제 목록은 data.content에 있을 수 있음
        const faqList = data.content || data;
        setFaqs(faqList);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchFaqs();
  }, [page, size]);

  if (loading) return <p className="loading">Loading...</p>;
  if (error) return <p className="error">Error: {error}</p>;
  if (!faqs || faqs.length === 0) return <p className="no-notices">QnA 내역이 없습니다.</p>;

  return (
    <div className="qna-list-container">
      <h2 className="qna-title">QnA 목록</h2>
      <table className="qna-table">
        <thead>
          <tr>
            <th className="table-header">번호</th>
            <th className="table-header">작성자</th>
            <th className="table-header">제목</th>
            <th className="table-header">등록일</th>
          </tr>
        </thead>
        <tbody>
          {faqs.map((faq) => (
            <tr key={faq.id} className="table-row">
              <td className="table-cell">{faq.id}</td>
              <td className="table-cell">{faq.nickname}</td>
              {/* 제목 클릭 시 상세 보기 페이지로 이동 */}
              <td className="table-cell">
                <Link to={`/faq/${faq.id}`} className="qna-link">
                  {faq.title}
                </Link>
              </td>
              <td className="table-cell">{formatDate(faq.create_at)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 페이징 버튼 */}
      <div className="pagination">
        <button
          onClick={() => setPage((prev) => (prev > 0 ? prev - 1 : 0))}
          disabled={page === 0}
          className="pagination-button"
        >
          이전
        </button>
        <span className="pagination-info">페이지: {page + 1}</span>
        <button
          onClick={() => setPage((prev) => prev + 1)}
          className="pagination-button"
        >
          다음
        </button>
      </div>
    </div>
  );
}

// 날짜 포맷 함수
function formatDate(dateString) {
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return dateString;
  return date.toLocaleString();
}

export default QnaList;