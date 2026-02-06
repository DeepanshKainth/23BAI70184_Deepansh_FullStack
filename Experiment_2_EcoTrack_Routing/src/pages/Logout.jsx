import { useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";

const Logout = () => {
  const { setIsAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    setIsAuthenticated(false);
  }, [setIsAuthenticated]);

  return (
    <div style={{ textAlign: "center", padding: "2rem" }}>
      <h3>You have been logged out successfully</h3>
      <p>Thank you for using EcoTrack!</p>
      <Link to="/login" style={{ color: "#27ae60", textDecoration: "underline" }}>
        Go back to Login
      </Link>
    </div>
  );
};

export default Logout;
