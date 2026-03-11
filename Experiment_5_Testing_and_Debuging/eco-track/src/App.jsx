import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AuthProvider from "./context/AuthProvider";
import ProtectedRoute from "./routes/ProtectedRoute";
import Layout from "./pages/Layout";
import useAuth from "./hooks/useAuth";
import Login from "./pages/Login";
import { Provider } from "react-redux";
import store from "./store/store"
import React from "react";
import Logs from "./data/logs";
import HeroSection from "./components/HeroSection";


function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      <Route element = {<Layout/>}>
        <Route
        path="/"
        element={
          user ? (
            <Navigate to="/home" replace />
          ) : (
            <Navigate to="/login" replace />
          )
        }
      />

      <Route path="/login" element={<Login />} />
      <Route element={<ProtectedRoute />}>
          <Route path="/home" element={<HeroSection/>} />
          <Route path = "/logs" element = {<Logs/>}></Route>
      </Route>
      </Route>
    </Routes>
  );
}

export default function App() {
  return (
    <Provider store = {store} >
      <AuthProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </AuthProvider>
    </Provider>
  );
}
