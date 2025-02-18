// src/components/JoinForm.jsx
import React, { useState } from 'react';
import '../styles/JoinForm.css'; // 커스텀 CSS 파일 임포트

function JoinForm() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    nickname: '',
    address: ''
  });

  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  // 필드 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // 회원가입 제출
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // POST /join로 MemberJoinDto 전송
    // MemberJoinDto = { email, password, nickname, address }
    try {
      const response = await fetch('http://localhost:8080/member/join', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (response.status === 201) {
        const text = await response.text(); // "회원가입 완료"
        setMessage(text);
        setError(null);
      } else {
        const text = await response.text(); // 에러 메시지(또는 예외 메세지) 수신
        throw new Error(`서버 오류: ${response.status} - ${text}`);
      }
    } catch (err) {
      setError(err.message);
      setMessage(null);
    }
  };

  return (
    <div className="join-form-container">
      <h2 className="form-title">회원가입</h2>
      
      {message && <p className="form-message">{message}</p>}
      {error && <p className="form-error">{error}</p>}

      <form onSubmit={handleSubmit} className="join-form">
        <div className="form-group">
          <label htmlFor="email" className="form-label">이메일:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            className="form-input"
            placeholder="이메일을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="password" className="form-label">비밀번호 (8~15자):</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            className="form-input"
            placeholder="비밀번호를 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="nickname" className="form-label">닉네임 (3~15자, 알파벳, 숫자, _-만 허용):</label>
          <input
            type="text"
            id="nickname"
            name="nickname"
            value={formData.nickname}
            onChange={handleChange}
            required
            className="form-input"
            placeholder="닉네임을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="address" className="form-label">주소:</label>
          <input
            type="text"
            id="address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            required
            className="form-input"
            placeholder="주소를 입력하세요"
          />
        </div>
        <button type="submit" className="form-button">회원가입</button>
      </form>
    </div>
  );
}

export default JoinForm;