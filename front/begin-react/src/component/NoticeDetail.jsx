// src/components/NoticeDetail.jsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

// 공지사항 상세 조회 (GET /notice/{id})
async function fetchNoticeDetail(noticeId) {
  const response = await fetch(`http://localhost:8080/notice/${noticeId}`);
  if (!response.ok) {
    throw new Error(`공지사항 조회 실패: ${response.status}`);
  }
  return await response.json();
}

function NoticeDetail() {
  const { id } = useParams(); // URL 경로에서 :id 추출
  const [notice, setNotice] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id) return;
    fetchNoticeDetail(id)
      .then((data) => {
        // data 예: { id, nickname, title, content, create_at }
        setNotice(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [id]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: 'red' }}>{error}</p>;
  if (!notice) return <p>Notice not found</p>;

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto' }}>
      <h2>공지사항 상세</h2>
      <div style={{ marginBottom: '10px' }}>
        <strong>번호:</strong> {notice.id}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>작성자:</strong> {notice.nickname}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>제목:</strong> {notice.title}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>등록일:</strong> {formatDate(notice.create_at)}
      </div>
      <div style={{ marginBottom: '10px' }}>
        <strong>내용:</strong> {notice.content || '(내용없음)'}
      </div>
    </div>
  );
}

// 날짜 포맷 함수 (재사용 가능)
function formatDate(dateTimeString) {
  const date = new Date(dateTimeString);
  if (isNaN(date.getTime())) return dateTimeString;
  return date.toLocaleString();
}

export default NoticeDetail;