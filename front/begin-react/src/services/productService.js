// src/services/productService.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/product';

// 제품 목록 조회 (페이지네이션)
// 만약 API 응답 구조가 { content: [...] } 형식이라면 content 속성을 리턴
export const getAllProducts = async (page = 0, size = 20) => {
  try {
    const response = await axios.get(API_BASE_URL, {
      params: { page, size }
    });
    // 응답 데이터가 PagedModel 형식일 경우, 보통 제품 배열은 response.data.content에 있음
    return response.data.content || response.data;
  } catch (error) {
    throw error;
  }
};

// 제품 상세 조회
export const getProductById = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 제품 등록 (옵션: ProductRegistRequest)
// 실제 사용 시, 백엔드에서 요구하는 필드에 맞게 수정
export const addProduct = async (productData) => {
  try {
    const response = await axios.post(API_BASE_URL, productData);
    return response.data;
  } catch (error) {
    throw error;
  }
};