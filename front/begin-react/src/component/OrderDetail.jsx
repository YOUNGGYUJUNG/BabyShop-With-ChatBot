// src/components/OrderDetail.jsx
import React, { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

function OrderDetail() {
  const { orderId } = useParams();
  const { token } = useContext(AuthContext);
  
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!orderId) return;
    
    const fetchOrderDetail = async () => {
      try {
        const response = await fetch(`http://localhost:8080/orders/${orderId}`, {
          headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
          }
        });
        if (!response.ok) {
          throw new Error(`서버 오류: ${response.status}`);
        }
        const data = await response.json();
        setOrder(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    
    fetchOrderDetail();
  }, [orderId, token]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: 'red' }}>Error: {error}</p>;
  if (!order) return <p>주문 상세 정보를 찾을 수 없습니다.</p>;

  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
      <h2>주문 상세 내역</h2>
      <p>
        <strong>주문 날짜:</strong> {order.orderDate}
      </p>
      <p>
        <strong>총 수량:</strong> {order.totalquantity}
      </p>
      <p>
        <strong>총 가격:</strong> {order.totalprice} 원
      </p>

      <h3>주문 상품 목록</h3>
      <table style={tableStyle}>
        <thead>
          <tr style={headerRowStyle}>
            <th style={headerCellStyle}>상품 번호</th>
            <th style={headerCellStyle}>상품명</th>
            <th style={headerCellStyle}>단가 (원)</th>
            <th style={headerCellStyle}>수량</th>
          </tr>
        </thead>
        <tbody>
          {order.orderProducts.map((prod, index) => (
            <tr key={index}>
              <td style={cellStyle}>{prod.id}</td>
              <td style={cellStyle}>{prod.productName}</td>
              <td style={cellStyle}>{prod.price}</td>
              <td style={cellStyle}>{prod.quantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const tableStyle = {
  width: '100%',
  borderCollapse: 'collapse',
  marginTop: '20px'
};

const headerRowStyle = {
  backgroundColor: '#f2f2f2'
};

const headerCellStyle = {
  border: '1px solid #ccc',
  padding: '8px',
  textAlign: 'center'
};

const cellStyle = {
  border: '1px solid #ccc',
  padding: '8px',
  textAlign: 'center'
};

export default OrderDetail;