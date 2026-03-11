import { createContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const AuthContext = createContext(null);

export function AuthContextProvider({ children }) {
  const navigate = useNavigate();

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const storedValue = localStorage.getItem("LoggedIn");
    setIsAuthenticated(storedValue === "true");
  }, []);

  function handleLogin() {
    setIsAuthenticated(true);
    localStorage.setItem("LoggedIn", "true");
    navigate("/dashboard");
  }

  function handleLogout() {
    setIsAuthenticated(false);
    localStorage.removeItem("LoggedIn");
    navigate("/login");
  }

  return (
    <AuthContext.Provider
      value={{ isAuthenticated, handleLogin, handleLogout }}
    >
      {children}
    </AuthContext.Provider>
  );
}