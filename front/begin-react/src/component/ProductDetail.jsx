// src/components/ProductDetail.jsx
import React, { useEffect, useState, useContext } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getProductById } from '../services/productService';
import { AuthContext } from '../context/AuthContext';
import styles from '../styles/ProductDetail.module.css'; // CSS 모듈 임포트

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { token } = useContext(AuthContext);
  
  useEffect(() => {
    getProductById(id)
      .then((data) => {
        setProduct(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [id]);

  // "장바구니에 추가" 버튼 클릭 시 호출되는 함수
  const handleAddToCart = async () => {
    if (!product) {
      alert('상품 정보를 불러오지 못했습니다.');
      return;
    }
    
    // DTO 형식에 맞게 데이터 구성 (필요한 필드명을 확인)
    const requestData = {
      productId: product.id,
      cartQnt: 1 // 기본 1개 추가 (수량 조절이 필요하면 인풋을 추가)
    };

    try {
      const response = await fetch('http://localhost:8080/cart', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` })
        },
        body: JSON.stringify(requestData)
      });
      if (!response.ok) {
        const text = await response.text();
        throw new Error(`장바구니 추가 실패: ${response.status} - ${text}`);
      }
      alert('장바구니에 추가되었습니다.');
    } catch (err) {
      alert(err.message);
    }
  };

  if (loading) return <p className={styles.loading}>장바구니 로딩 중...</p>;
  if (error) return <p className={styles.error}>Error: {error}</p>;
  if (!product) return <p className={styles.noProduct}>Product not found</p>;

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{product.productName}</h2>
      <div className={styles.productContent}>
        {product.image && (
          <img
            src={product.image}
            alt={product.productName}
            className={styles.productImage}
          />
        )}
        <div className={styles.productInfo}>
          <p><strong>수량:</strong> {product.qnt}</p>
          <p><strong>가격:</strong> {product.price.toLocaleString()} 원</p>
          <p><strong>판매 상태:</strong> {product.isSold ? '판매 완료' : '판매 중'}</p>
          <p><strong>카테고리:</strong> {product.categoryName}</p>
          <p className={styles.description}><strong>설명:</strong> {product.description}</p>
          
          {/* 주문하기 링크 및 장바구니에 추가 버튼 */}
          <div className={styles.actionButtons}>
            <Link to={`/direct/order/${product.id}`} className={styles.orderLink}>
              이 상품 주문하기
            </Link>
            <button onClick={handleAddToCart} className={styles.addToCartButton}>
              장바구니에 추가
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;