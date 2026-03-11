import { memo } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

// Memoized ProtectedRoute component to prevent unnecessary re-renders
const ProtectedRoute = memo(({ children }) => {
  const auth = useAuth();

  if (!auth.isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
});

ProtectedRoute.displayName = "ProtectedRoute";

export default ProtectedRoute;