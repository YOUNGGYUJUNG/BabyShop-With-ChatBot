
// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './component/Header';
import ProductList from './component/ProductList';
import ProductDetail from './component/ProductDetail';
import ProductForm from './component/ProductForm';
import DirectOrderForm from './component/DirectOrderForm'
import MainPage from './component/MainPage';
import NoticeList from './component/NoticeList';
import NoticeDetail from './component/NoticeDetail';
import JoinForm from './component/JoinForm';
import LoginForm from './component/LoginForm';
import { AuthProvider } from './context/AuthContext';
import OrderList from './component/OrderList';
import SimpleChatComponent from './component/SimpleChatComponent';
import FaqList from './component/FaqList';
import FaqDetail from './component/FaqDetail';
import CartList from './component/CartList';
import CartOrderPage from './component/CartOrderPage';
import OrderDetail from './component/OrderDetail';
import { Footer } from './component/Footer';


function App() {
  return (
    <AuthProvider>
      <Router>
      <Header />
      <div className="App">
        {/* 사이트 내 네비게이션 추가 가능 */}
        <Routes>

          <Route path="/" element={<MainPage />} />
          
          <Route path="/join" element={<JoinForm/>}/>
          <Route path="/login" element={<LoginForm/>}/>

          {/* <Route path="/" element={<MainPage />} /> */}
          <Route path="/products" element={<ProductList />} />
          <Route path="/product/:id" element={<ProductDetail />} />
          {/* 제품 등록/수정 페이지 */}
          <Route path="/product/form" element={<ProductForm />} />
          <Route path="*" element={<div>404 Not Found</div>} />
          
          <Route path="/cart" element={<CartList/>} />
          <Route path="/cart/order" element={<CartOrderPage />} />
          <Route path="/notice" element={<NoticeList />} />
          <Route path="/notice/:id" element={<NoticeDetail />} />

          <Route path="/faq" element={<FaqList />} />
          <Route path="/faq/:id" element={<FaqDetail/>} />

          
          <Route path="/direct/order/:id" element={<DirectOrderForm />}/>
          <Route path="/orders" element={<OrderList />}/>
          
          <Route path="/orders/:orderid" element={<OrderDetail/>} />


          <Route path="/chat" element={<SimpleChatComponent />} />
        </Routes>
      </div>
      <Footer />
    </Router>
    </AuthProvider>
  );
}

export default App;