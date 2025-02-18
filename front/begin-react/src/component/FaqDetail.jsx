// src/components/QnaDetail.jsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

// FAQ 상세 조회 (GET /faq/{id})
async function fetchFaqDetail(faqId) {
  const response = await fetch(`http://localhost:8080/faq/${faqId}`);
  if (!response.ok) {
    throw new Error(`FAQ 조회 실패: ${response.status}`);
  }
  return await response.json(); // 예: { id, nickname, title, content, create_at }
}

function QnaDetail() {
  // URL 경로에서 FAQ id 추출
  const { id } = useParams();
  const [faq, setFaq] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id) return;
    fetchFaqDetail(id)
      .then((data) => {
        setFaq(data);
      })
      .catch((err) => {
        setError(err.message);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: 'red' }}>Error: {error}</p>;
  if (!faq) return <p>FAQ 정보를 찾을 수 없습니다.</p>;

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <h2>FAQ 상세</h2>
      <div style={{ marginBottom: '10px' }}>
        <strong>번호:</strong> {faq.id}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>작성자:</strong> {faq.nickname}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>제목:</strong> {faq.title}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>등록일:</strong> {formatDate(faq.create_at)}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>내용:</strong> {faq.content || '(내용없음)'}
      </div>
    </div>
  );
}

// 단순 날짜 포맷 함수
function formatDate(dateString) {
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return dateString;
  return date.toLocaleString();
}

export default QnaDetail;