// src/components/LoginForm.jsx
import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import {jwtDecode} from 'jwt-decode'; // 디폴트 임포트로 수정
import { useNavigate } from 'react-router-dom';
import '../styles/LoginForm.css'; // 커스텀 CSS 파일 임포트

function LoginForm() {
  const { login } = useContext(AuthContext);
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/member/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });
      
      if (!response.ok) {
        const text = await response.text();
        throw new Error(`서버 오류: ${response.status} - ${text}`);
      }
      
      let token = response.headers.get('Authorization');
      if (token && token.startsWith('Bearer ')) {
        token = token.slice(7);
      }
      
      const resultStr = await response.text();
      
      if (token) {
        const payload = jwtDecode(token);
        // payload 구조에 따라 값 추출
        const nickname = (payload.member && payload.member.nickname) || payload.nickname || "";
        const exp = payload.exp || 0;
        const userId = (payload.member && payload.member.id) || payload.id || 0;
        
        // AuthContext의 login 함수 호출
        login(token, nickname, exp, userId);
      }
      
      setMessage(resultStr);
      setError(null);
      navigate("/");
    } catch (err) {
      setError(err.message);
      setMessage(null);
    }
  };

  return (
    <div className="login-form-container">
      <h2 className="form-title">로그인</h2>
      {message && <p className="form-message">{message}</p>}
      {error && <p className="form-error">{error}</p>}
      <form onSubmit={handleSubmit} className="login-form">
        <div className="form-group">
          <label htmlFor="email" className="form-label">이메일:</label>
          <input
            type="email"
            id="email"
            name="email"
            required
            value={formData.email}
            onChange={handleChange}
            className="form-input"
            placeholder="이메일을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="password" className="form-label">비밀번호:</label>
          <input
            type="password"
            id="password"
            name="password"
            required
            value={formData.password}
            onChange={handleChange}
            className="form-input"
            placeholder="비밀번호를 입력하세요"
          />
        </div>
        <button type="submit" className="form-button">로그인</button>
      </form>
    </div>
  );
}

export default LoginForm;