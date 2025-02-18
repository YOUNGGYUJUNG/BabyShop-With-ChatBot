// src/components/DirectOrderForm.jsx
import React, { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import '../styles/DirectOrderForm.css'; // 커스텀 CSS 파일 임포트

// 상품 상세 불러오기 (GET /product/{id})
async function fetchProduct(id) {
  const response = await fetch(`http://localhost:8080/product/${id}`);
  if (!response.ok) {
    throw new Error(`상품 조회 실패: ${response.status}`);
  }
  return await response.json(); // 예: { id, productName, price, qnt, ... }
}

function DirectOrderForm() {
  // URL 파라미터에서 상품 ID 추출
  const { id: routeProductId } = useParams();
  const { token, isLoggedIn } = useContext(AuthContext);

  // 상품 정보 (기본 단가 포함)
  const [productName, setProductName] = useState('');
  const [basePrice, setBasePrice] = useState(0);
  const [productId, setProductId] = useState(null);

  // 주문 폼 상태
  const [quantity, setQuantity] = useState(1);

  // 서버 응답 또는 에러
  const [orderSuccess, setOrderSuccess] = useState(false);
  const [error, setError] = useState(null);

  // 로딩 상태 (선택사항)
  const [loading, setLoading] = useState(true);

  // (1) 컴포넌트 마운트/routeProductId 변경 시: 상품 상세 불러오기
  useEffect(() => {
    if (!routeProductId) return; // param이 없으면 스킵
    fetchProduct(routeProductId)
      .then((data) => {
        // 예: data = { id: 17, productName: "샘플", price: 10000, ... }
        setProductId(data.id);
        setProductName(data.productName);
        setBasePrice(data.price);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [routeProductId]);

  // (2) 주문 제출 핸들러: POST /orders
  // OrderCreateRequestDto = { orderDate, products: [ { productId, quantity, price }, ... ] }
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isLoggedIn) {
      setError("로그인 후 주문 가능합니다.");
      return;
    }

    // 자동 계산된 총 금액: basePrice * quantity
    const totalPrice = basePrice * quantity;

    // 주문 생성 DTO
    // 단일 상품이라 products 배열에 한 개만 넣습니다.
    const requestBody = {
      orderDate: new Date().toISOString(),
      products: [{ productId, quantity, price: totalPrice }]
    };

    try {
      const response = await fetch('http://localhost:8080/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token // 토큰 사용
        },
        body: JSON.stringify(requestBody)
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(`Server error: ${response.status} - ${text}`);
      }

      // 성공 (백엔드에서 HttpStatus.OK를 반환)
      setOrderSuccess(true);
      setError(null);
    } catch (err) {
      setError(err.message);
      setOrderSuccess(false);
    }
  };

  // (3) 총 금액 (단가 * 수량)
  const totalAmount = basePrice * quantity;

  // (4) 화면 렌더링
  if (loading) return <p className="loading">Loading...</p>;
  if (error && !orderSuccess) return <p className="error">Error: {error}</p>;

  return (
    <div className="direct-order-form-container">
      <h2 className="form-title">주문하기</h2>

      {orderSuccess && (
        <p className="success-message">
          주문이 완료되었습니다!
        </p>
      )}

      {/* 주문 전이라면 폼 표시 */}
      {!orderSuccess && (
        <form onSubmit={handleSubmit} className="direct-order-form">
          {productName && (
            <div className="form-group">
              <label htmlFor="productName" className="form-label">상품명:</label>
              <span className="form-value">{productName}</span>
            </div>
          )}

          <div className="form-group">
            <label htmlFor="basePrice" className="form-label">단가:</label>
            <span className="form-value">{basePrice.toLocaleString()} 원</span>
          </div>

          <div className="form-group">
            <label htmlFor="quantity" className="form-label">수량:</label>
            <input
              type="number"
              id="quantity"
              name="quantity"
              value={quantity}
              onChange={(e) => setQuantity(Number(e.target.value))}
              min="1"
              required
              className="form-input"
              placeholder="수량을 입력하세요"
            />
          </div>

          <div className="form-group total-amount">
            <label className="form-label">총금액:</label>
            <span className="form-value">{totalAmount.toLocaleString()} 원</span>
          </div>

          <button type="submit" className="submit-button">
            주문 완료
          </button>
        </form>
      )}
    </div>
  );
}

export default DirectOrderForm;