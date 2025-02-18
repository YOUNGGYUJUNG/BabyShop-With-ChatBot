// src/components/CartListOrder.jsx
import React, { useEffect, useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom'; // 추가
import styles from '../styles/CartListOrder.module.css'; // CSS 모듈 임포트

function CartListOrder() {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [updateMsg, setUpdateMsg] = useState(null);
  const { token, userId } = useContext(AuthContext);
  const navigate = useNavigate(); // 추가

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

  // 컴포넌트 마운트 시 장바구니 항목 불러오기
  useEffect(() => {
    if (userId) {
      fetchCartItems();
    }
  }, [token, userId]);

  // 수량 변경 핸들러
  const handleLocalQuantityChange = (id, newQuantity) => {
    setCartItems((prev) =>
      prev.map((item) =>
        item.id === id ? { ...item, newQuantity: newQuantity } : item
      )
    );
  };

  // 수량 변경 요청 핸들러
  const handleQuantityUpdate = async (item) => {
    const updatedQuantity = item.newQuantity || item.quantity;
    const updateData = {
      id: item.id,
      quantity: updatedQuantity
    };

    try {
      const response = await fetch('http://localhost:8080/cart', {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` })
        },
        body: JSON.stringify(updateData)
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`수량 변경 실패: ${response.status} - ${errorText}`);
      }
      setUpdateMsg(`상품 ${item.productName}의 수량이 변경되었습니다.`);
      fetchCartItems();
    } catch (err) {
      setError(err.message);
    }
  };

  // 삭제 요청 핸들러
  const handleDelete = async (itemId) => {
    const deleteData = { cartItemId: itemId };
    try {
      const response = await fetch('http://localhost:8080/cart', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` })
        },
        body: JSON.stringify(deleteData)
      });
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`삭제 실패: ${response.status} - ${errorText}`);
      }
      fetchCartItems();
    } catch (err) {
      setError(err.message);
    }
  };

  // "주문하기" 버튼 클릭 시 주문 페이지로 이동
  const handleOrderClick = () => {
    navigate('/cart/order'); // 주문 페이지 경로로 변경
  };

  if (loading) return <p className={styles.loading}>Loading...</p>;
  if (error) return <p className={styles.error}>Error: {error}</p>;
  if (!cartItems || cartItems.length === 0)
    return <p className={styles.noItems}>장바구니에 항목이 없습니다.</p>;

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>장바구니 목록 (수량 수정 및 삭제 가능)</h2>
      {updateMsg && <p className={styles.updateMsg}>{updateMsg}</p>}
      <table className={styles.cartTable}>
        <thead>
          <tr>
            <th>번호</th>
            <th>상품 ID</th>
            <th>상품명</th>
            <th>현재 수량</th>
            <th>총 가격 (원)</th>
            <th>수량 수정</th>
            <th>삭제</th>
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
              <td>
                <input
                  type="number"
                  defaultValue={item.quantity}
                  min="1"
                  className={styles.quantityInput}
                  onChange={(e) =>
                    handleLocalQuantityChange(item.id, Number(e.target.value))
                  }
                />
                <button
                  onClick={() => handleQuantityUpdate(item)}
                  className={styles.updateButton}
                >
                  변경
                </button>
              </td>
              <td>
                <button
                  onClick={() => handleDelete(item.id)}
                  className={styles.deleteButton}
                >
                  삭제
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {/* "주문하기" 버튼 추가 */}
      <div className={styles.orderButtonContainer}>
        <button onClick={handleOrderClick} className={styles.orderButton}>
          주문하기
        </button>
      </div>
    </div>
  );
}

export default CartListOrder;