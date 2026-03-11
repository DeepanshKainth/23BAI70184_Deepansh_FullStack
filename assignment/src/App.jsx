import { useMemo, useState } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { ThemeProvider, createTheme, CssBaseline } from "@mui/material";
import LoginPage from "./pages/login";
import Dashboard from "./pages/dashboard";
import DashboardWater from "./pages/water";
import { AuthContextProvider } from "./context/authContext";
import AuthProtectedRoute from "./protectedRoutes/authProtectedRoute";

function App() {
  const [mode, setMode] = useState("dark");

  const theme = useMemo(() =>
    createTheme({
      palette: {
        mode: mode,
      },
    }),
  [mode]);


  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />

      <BrowserRouter>
      <AuthContextProvider>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          
          <Route path="/login" element={<LoginPage />} />

          <Route
            path="/dashboard"
            element={
              <AuthProtectedRoute>
                <Dashboard />
              </AuthProtectedRoute>
            }
          >
            <Route path="water" element={<DashboardWater />} />
          </Route>
      </Routes>
      </AuthContextProvider>
      </BrowserRouter>

    </ThemeProvider>
  );
}

export default App;