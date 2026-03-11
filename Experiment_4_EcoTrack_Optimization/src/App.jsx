import { Suspense, lazy } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import ProtectedRoute from "./routes/ProtectedRoute.jsx";
import { Box, CircularProgress, Container } from "@mui/material";

// Lazy load page components for code splitting
// This reduces the initial bundle size and improves load performance
const Login = lazy(() => import("./pages/login"));
const Logout = lazy(() => import("./pages/Logout"));
const DashboardLayout = lazy(() => import("./pages/DashboardLayout"));
const DashboardSummary = lazy(() => import("./pages/DashboardSummary"));
const DashboardAnalytics = lazy(() => import("./pages/DashboardAnalytics"));
const Logs = lazy(() => import("./pages/Logs"));

// Loading component displayed while lazy-loaded components are loading
const LoadingFallback = () => {
  console.log('⏳ Loading lazy-loaded component chunk...');
  return (
    <Container maxWidth="lg">
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "400px",
        }}
      >
        <CircularProgress size={50} />
      </Box>
    </Container>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Header title="EcoTrack" />
      <Suspense fallback={<LoadingFallback />}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/logout" element={<Logout />} />
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <DashboardLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<DashboardSummary />} />
            <Route path="summary" element={<DashboardSummary />} />
            <Route path="analytics" element={<DashboardAnalytics />} />
            <Route path="logs" element={<Logs />} />
          </Route>
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}

export default App;
