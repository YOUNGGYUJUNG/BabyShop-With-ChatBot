// src/components/Footer.jsx
import React from 'react';
import { Link } from 'react-router-dom'; // react-router-dom의 Link 사용
import '../styles/Footer.css'; // 커스텀 CSS 파일 임포트

export function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h3 className="footer-title">아기용품샵</h3>
          <p className="footer-text">우리 아기에게 필요한 모든 것을 만나보세요!</p>
        </div>
        <div className="footer-section">
          <h3 className="footer-title">빠른 링크</h3>
          <ul className="footer-links">
            <li className="footer-link-item">
              <Link to="/about" className="footer-link">
                회사 소개
              </Link>
            </li>
            <li className="footer-link-item">
              <Link to="/contact" className="footer-link">
                문의하기
              </Link>
            </li>
            <li className="footer-link-item">
              <Link to="/faq" className="footer-link">
                자주 묻는 질문
              </Link>
            </li>
          </ul>
        </div>
        <div className="footer-section">
          <h3 className="footer-title">고객센터</h3>
          <p className="footer-text">전화: 1234-5678</p>
          <p className="footer-text">이메일: support@babyshop.com</p>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2023 아기용품샵. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;