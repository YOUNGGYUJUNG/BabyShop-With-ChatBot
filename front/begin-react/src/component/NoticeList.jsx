// src/components/NoticeList.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/NoticeList.css'; // 커스텀 CSS 파일 임포트

function NoticeList() {
  const [notices, setNotices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 페이지/사이즈 상태
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);

  useEffect(() => {
    const url = `http://localhost:8080/notice?page=${page}&size=${size}`;
    fetch(url)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`서버 오류: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        const content = data.content || data;
        setNotices(content);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [page, size]);

  if (loading) return <p className="loading">Loading...</p>;
  if (error) return <p className="error">Error: {error}</p>;
  if (!notices || notices.length === 0) return <p className="no-notices">No notices found</p>;

  return (
    <div className="notice-list-container">
      <h2 className="notice-title">공지사항 목록</h2>
      <table className="notice-table">
        <thead>
          <tr>
            <th className="table-header">번호</th>
            <th className="table-header">작성자</th>
            <th className="table-header">제목</th>
            <th className="table-header">등록일</th>
          </tr>
        </thead>
        <tbody>
          {notices.map((notice) => (
            <tr key={notice.id}>
              <td className="table-cell">{notice.id}</td>
              <td className="table-cell">{notice.nickname}</td>
              {/* 제목 클릭 시 상세 보기 페이지로 이동 */}
              <td className="table-cell">
                <Link to={`/notice/${notice.id}`} className="notice-link">
                  {notice.title}
                </Link>
              </td>
              <td className="table-cell">{formatDate(notice.create_at)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 페이징 */}
      <div className="pagination">
        <button
          onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
          disabled={page === 0}
          className="pagination-button"
        >
          이전
        </button>
        <span className="pagination-info">페이지: {page}</span>
        <button onClick={() => setPage((prev) => prev + 1)} className="pagination-button">
          다음
        </button>
      </div>
    </div>
  );
}

// 날짜 포맷 함수
function formatDate(dateTimeString) {
  const date = new Date(dateTimeString);
  if (isNaN(date.getTime())) return dateTimeString;
  return date.toLocaleString();
}

export default NoticeList;