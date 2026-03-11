import React, { useContext } from 'react'
import { AuthContext } from '../context/authContext'
import { Navigate, useNavigate } from 'react-router-dom';

export default function AuthProtectedRoute({children}) {
  const isAuthenticated = useContext(AuthContext); 

  if(!isAuthenticated){
        return <Navigate to="/login" replace />;
    }
  
    return children; 
  
}
