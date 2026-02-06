import DashboardAnalytics from "./pages/DashboardAnalytics";
import logs from "./data/logs";
import Logs from "./pages/Logs";
import DashboardLayout from "./pages/DashboardLayout";
import DashboardSummary from "./pages/DashboardSummary";
import ProtectedRoute from "./routes/ProtectedRoute.jsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/login";
import Logout from "./pages/Logout";
import Header from "./components/Header";

function App() {
  return (
    <BrowserRouter>
      <Header title="EcoTrack" />
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
    </BrowserRouter>
  );
}

export default App;
