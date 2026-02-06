import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Header = ({ title }) => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    navigate("/logout");
  };

  return (
    <header
      style={{
        padding: "1rem",
        backgroundColor: "#27ae60",
        textAlign: "center",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <h1>{title || "EcoTrack"}</h1>
      {isAuthenticated && (
        <button
          onClick={handleLogout}
          style={{
            padding: "0.5rem 1rem",
            backgroundColor: "#e74c3c",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          Logout
        </button>
      )}
    </header>
  );
};

export default Header;
