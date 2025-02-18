// src/components/ProductForm.jsx
import React, { useState } from 'react';
import { addProduct } from '../services/productService';
import '../styles/ProductForm.css'; // 커스텀 CSS 파일 임포트

const ProductForm = () => {
  const [formData, setFormData] = useState({
    product_name: '',
    qnt: 1,
    price: 0,
    image: '',
    description: '',
    categoryId: ''
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // addProduct 함수는 백엔드의 제품 추가 API (/product) 호출
      await addProduct(formData);
      setMessage('제품 등록이 완료되었습니다.');
      setError('');
      setFormData({
        product_name: '',
        qnt: 1,
        price: 0,
        image: '',
        description: '',
        categoryId: ''
      });
    } catch (err) {
      setError('제품 등록에 실패했습니다: ' + err.message);
      setMessage('');
    }
  };

  return (
    <div className="product-form-container">
      <h2 className="form-title">제품 등록</h2>
      <form onSubmit={handleSubmit} className="product-form">
        <div className="form-group">
          <label htmlFor="product_name" className="form-label">제품명:</label>
          <input
            type="text"
            id="product_name"
            name="product_name"
            value={formData.product_name}
            onChange={handleChange}
            required
            className="form-input"
            placeholder="제품명을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="qnt" className="form-label">수량:</label>
          <input
            type="number"
            id="qnt"
            name="qnt"
            value={formData.qnt}
            onChange={handleChange}
            required
            min="1"
            className="form-input"
            placeholder="수량을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="price" className="form-label">가격:</label>
          <input
            type="number"
            id="price"
            name="price"
            value={formData.price}
            onChange={handleChange}
            required
            min="1"
            className="form-input"
            placeholder="가격을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="image" className="form-label">이미지 URL:</label>
          <input
            type="text"
            id="image"
            name="image"
            value={formData.image}
            onChange={handleChange}
            className="form-input"
            placeholder="이미지 URL을 입력하세요"
          />
        </div>
        <div className="form-group">
          <label htmlFor="description" className="form-label">설명:</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="form-textarea"
            placeholder="제품 설명을 입력하세요"
          ></textarea>
        </div>
        <div className="form-group">
          <label htmlFor="categoryId" className="form-label">카테고리 ID:</label>
          <input
            type="number"
            id="categoryId"
            name="categoryId"
            value={formData.categoryId}
            onChange={handleChange}
            required
            className="form-input"
            placeholder="카테고리 ID를 입력하세요"
          />
        </div>
        <button type="submit" className="form-button">등록</button>
      </form>
      {message && <p className="form-message">{message}</p>}
      {error && <p className="form-error">{error}</p>}
    </div>
  );
};

export default ProductForm;