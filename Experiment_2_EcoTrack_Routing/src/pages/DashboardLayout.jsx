import { Link, Outlet } from "react-router-dom";

const DashboardLayout = () => {
  return (
    <>
      <h3>Dashboard</h3>
      <nav style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
        <Link to="/summary">Summary</Link>
        <Link to="/analytics">Analytics</Link>
        <Link to="/logs">Logs</Link>
      </nav>
      <hr />
      <Outlet />
    </>
  );
};

export default DashboardLayout;