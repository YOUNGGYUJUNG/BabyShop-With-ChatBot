// src/components/MainPage.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/MainPage.css'; // 별도의 CSS 파일

function MainPage() {
  return (
    <div className="main-page">
      <h1 className="main-title">아기용품샵</h1>
      <p className="main-subtitle">우리 아기에게 필요한 모든 것을 만나보세요!</p>

      <div className="button-group">
        {/* 링크 버튼들 */}
        <Link to="/products" className="main-button">
          상품 전체보기
        </Link>
        <Link to="/login" className="main-button">
          로그인
        </Link>
        <Link to="/join" className="main-button">
          회원가입
        </Link>
        <Link to="/notice" className="main-button">
          게시판
        </Link>
          <Link to="/chat" className="main-button">
              챗봇
            </Link>
      </div>
    </div>
  );
}

// 간단한 인라인 스타일은 제거하고 CSS 파일에서 스타일링
export default MainPage;