// src/components/ProductList.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllProducts } from '../services/productService';
import '../styles/ProductList.css'; // 커스텀 CSS 파일 임포트

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // 페이지네이션: 첫 페이지(0), 한 페이지당 20개 제품 (필요에 따라 조정)
    getAllProducts(0, 20)
      .then((data) => {
        setProducts(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <p className="loading">Loading...</p>;
  if (error) return <p className="error">Error: {error}</p>;
  if (!products || products.length === 0) return <p className="no-products">No products found</p>;

  return (
    <div className="product-list-container">
      <h2 className="list-title">제품 목록</h2>
      <div className="product-grid">
        {products.map((product) => (
          <div key={product.id} className="product-card">
            {product.image && (
              <img
                src={product.image}
                alt={product.productName}
                className="product-image"
              />
            )}
            <h3 className="product-name">{product.productName}</h3>
            <p className="product-quantity"><strong>수량:</strong> {product.qnt}</p>
            <p className="product-price"><strong>가격:</strong> {product.price.toLocaleString()} 원</p>
            <p className="product-status"><strong>판매 상태:</strong> {product.isSold ? '판매 완료' : '판매 중'}</p>
            <p className="product-category"><strong>카테고리:</strong> {product.categoryName}</p>
            <p className="product-description">{product.description}</p>
            <Link to={`/product/${product.id}`} className="product-detail-link">제품 상세 보기</Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;