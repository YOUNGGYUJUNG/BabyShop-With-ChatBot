// src/context/AuthContext.js
import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [userId, setUserId] = useState(null);
  const [nickname, setNickname] = useState(null);
  const [token, setToken] = useState(null);
  const [tokenExp, setTokenExp] = useState(null); // 토큰 만료시간

  const isLoggedIn = !!token;

  const login = (newToken, newNickname, newExp, newUserId) => {
    setToken(newToken);
    setNickname(newNickname);
    setTokenExp(newExp);
    setUserId(newUserId);
    localStorage.setItem('jwtToken', newToken);
    localStorage.setItem('nickname', newNickname);
    localStorage.setItem('tokenExp', String(newExp));
    localStorage.setItem('userId', String(newUserId));
  };

  const logout = () => {
    setToken(null);
    setNickname(null);
    setTokenExp(null);
    setUserId(null);
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('nickname');
    localStorage.removeItem('tokenExp');
    localStorage.removeItem('userId');
  };

  useEffect(() => {
    const storedToken = localStorage.getItem('jwtToken');
    const storedNickname = localStorage.getItem('nickname');
    const storedExp = localStorage.getItem('tokenExp');
    const storedUserId = localStorage.getItem('userId');
    if (storedToken && storedNickname && storedExp && storedUserId) {
      setToken(storedToken);
      setNickname(storedNickname);
      setTokenExp(Number(storedExp));
      setUserId(Number(storedUserId));
    }
  }, []);

  return (
    <AuthContext.Provider value={{ userId, nickname, token, tokenExp, isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}