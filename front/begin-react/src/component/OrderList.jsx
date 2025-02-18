// src/components/OrderList.jsx
import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import styles from '../styles/OrderList.module.css'; // CSS 모듈 임포트

function OrderList() {
  const { token, isLoggedIn } = useContext(AuthContext);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 페이지 상태 관리
  const [page, setPage] = useState(0);
  const size = 10;

  useEffect(() => {
    if (!isLoggedIn || !token) {
      setError('로그인이 필요합니다.');
      setLoading(false);
      return;
    }

    const fetchOrders = async () => {
      try {
        const url = `http://localhost:8080/orders?page=${page}&size=${size}`;
        const response = await fetch(url, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        if (!response.ok) {
          throw new Error(`서버 오류: ${response.status}`);
        }
        const data = await response.json();
        // 실제 주문 목록은 data.content에 있을 수 있습니다.
        const orderList = data.content || data;
        setOrders(orderList);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchOrders();
  }, [isLoggedIn, token, page]);

  const handlePrevious = () => {
    if (page > 0) setPage(page - 1);
  };

  const handleNext = () => {
    setPage(page + 1);
  };

  if (loading) return <p className={styles.loading}>Loading...</p>;
  if (error) return <p className={styles.error}>Error: {error}</p>;
  if (!orders || orders.length === 0) return <p className={styles.noOrders}>주문 내역이 없습니다.</p>;

  return (
    <div className={styles.orderListContainer}>
      <h2 className={styles.title}>주문 내역</h2>
      {orders.map((order) => (
        <div key={order.id} className={styles.orderCard}>
          <h3 className={styles.orderDate}>주문 날짜: {new Date(order.orderDate).toLocaleString()}</h3>
          <table className={styles.orderTable}>
            <thead>
              <tr>
                <th>주문 상품 번호</th>
                <th>상품 ID</th>
                <th>상품명</th>
                <th>단가 (원)</th>
                <th>수량</th>
              </tr>
            </thead>
            <tbody>
              {order.orderProducts.map((prod, idx) => (
                <tr key={idx}>
                  <td>{prod.id}</td>
                  <td>{prod.productId}</td>
                  <td>{prod.productName}</td>
                  <td>{prod.price.toLocaleString()}</td>
                  <td>{prod.quantity}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {/* 주문 상세 페이지로 이동하는 링크 추가 */}
          <div className={styles.detailLink}>
            <Link to={`/orders/${order.id}`} className={styles.link}>
              주문 상세 보기
            </Link>
          </div>
        </div>
      ))}

      {/* 페이지 네비게이션 */}
      <div className={styles.pagination}>
        <button
          onClick={handlePrevious}
          disabled={page === 0}
          className={`${styles.paginationButton} ${page === 0 ? styles.disabledButton : ''}`}
        >
          이전
        </button>
        <span className={styles.pageInfo}>페이지: {page + 1}</span>
        <button
          onClick={handleNext}
          className={styles.paginationButton}
        >
          다음
        </button>
      </div>
    </div>
  );
}

export default OrderList;