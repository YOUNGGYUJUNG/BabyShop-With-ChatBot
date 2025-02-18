// src/components/CartOrderPage.jsx
import React, { useEffect, useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/CartOrderPage.module.css'; // CSS 모듈 임포트

function CartOrderPage() {
  const [cartItems, setCartItems] = useState([]);
  const [orderSuccess, setOrderSuccess] = useState(false);
  const [orderMessage, setOrderMessage] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { token } = useContext(AuthContext);
  const navigate = useNavigate();

  // 장바구니 항목 가져오기
  const fetchCartItems = async () => {
    try {
      const response = await fetch('http://localhost:8080/cart', {
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` })
        }
      });
      if (!response.ok) {
        throw new Error(`서버 오류: ${response.status}`);
      }
      const data = await response.json();
      setCartItems(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCartItems();
  }, [token]);

  // 주문하기 버튼 클릭 시 처리
  const handleOrder = async () => {
    if (!cartItems || cartItems.length === 0) {
      setError('장바구니에 주문할 항목이 없습니다.');
      return;
    }

    // 각 장바구니 항목을 주문 요청에 맞게 매핑
    const products = cartItems.map((item) => ({
      productId: item.productId,
      quantity: item.quantity,
      price: item.price
    }));

    // 주문 요청 데이터 구성
    const orderRequestData = {
      orderDate: new Date().toISOString(),
      products: products
    };

    try {
      const response = await fetch('http://localhost:8080/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` })
        },
        body: JSON.stringify(orderRequestData)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`주문 실패: ${response.status} - ${errorText}`);
      }

      // 주문 성공 시 메시지 및 상태 업데이트
      const resultText = await response.text();
      setOrderSuccess(true);
      setOrderMessage(resultText);
      setError(null);

      // 주문 성공 후 페이지 전환 (예: 주문 목록 페이지로 이동)
      navigate('/orders'); // 사용자가 원하는 경로로 변경 가능
    } catch (err) {
      setError(err.message);
      setOrderSuccess(false);
      setOrderMessage(null);
    }
  };

  if (loading) return <p className={styles.loading}>장바구니 로딩 중...</p>;
  if (error) return <p className={styles.error}>Error: {error}</p>;
  if (!cartItems || cartItems.length === 0) return <p className={styles.noItems}>장바구니에 항목이 없습니다.</p>;

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>장바구니 주문</h2>
      <table className={styles.cartTable}>
        <thead>
          <tr>
            <th>번호</th>
            <th>상품 ID</th>
            <th>상품명</th>
            <th>수량</th>
            <th>총 가격 (원)</th>
          </tr>
        </thead>
        <tbody>
          {cartItems.map((item, index) => (
            <tr key={item.id}>
              <td>{index + 1}</td>
              <td>{item.productId}</td>
              <td>{item.productName}</td>
              <td>{item.quantity}</td>
              <td>{item.price.toLocaleString()} 원</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button onClick={handleOrder} className={styles.orderButton}>
        주문 하기
      </button>

      {orderSuccess && (
        <div className={styles.orderResult}>
          <h3>주문 결과</h3>
          <p>{orderMessage}</p>
        </div>
      )}
    </div>
  );
}

export default CartOrderPage;