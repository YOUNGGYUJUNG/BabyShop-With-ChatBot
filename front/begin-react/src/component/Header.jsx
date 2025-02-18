// src/components/Header.jsx
import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import '../styles/Header.css';

function Header() {
  const { isLoggedIn, nickname, tokenExp, logout, userId } = useContext(AuthContext);
  const [remainingSec, setRemainingSec] = useState(0);

  useEffect(() => {
    if (!isLoggedIn || !tokenExp) {
      setRemainingSec(0);
      return;
    }

    const intervalId = setInterval(() => {
      const now = Math.floor(Date.now() / 1000);
      const remain = tokenExp - now;
      setRemainingSec(remain > 0 ? remain : 0);
      if (remain <= 0) {
        logout();
      }
    }, 1000);

    return () => clearInterval(intervalId);
  }, [isLoggedIn, tokenExp, logout]);

  return (
    <header className="shop-header">
      <Link to="/" className="shop-title">
        육아용품샵
      </Link>
      <nav className="nav-links">
        <Link to="/" className="nav-link">홈</Link>
        {isLoggedIn ? (
          <>
            <span className="user-info">
              {nickname} 님 {remainingSec > 0 && ` (만료까지 ${remainingSec}초)`}
            </span>
            <Link to="/" onClick={logout} className="nav-link">로그아웃</Link>
            {userId && (
              <>
                <Link to="/orders" className="nav-link">주문 목록</Link>
                <Link to="/cart" className="nav-link">장바구니</Link>
              </>
            )}
          </>
        ) : (
          <>
            <Link to="/login" className="nav-link">로그인</Link>
            <Link to="/join" className="nav-link">회원가입</Link>
          </>
        )}
        <Link to="/notice" className="nav-link">게시판</Link>
        <Link to="/faq" className="nav-link">FAQ</Link>
      </nav>
    </header>
  );
}

export default Header;