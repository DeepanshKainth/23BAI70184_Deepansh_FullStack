import { memo } from "react";
import { Link, Outlet } from "react-router-dom";
import {
  Box,
  Container,
  Tabs,
  Tab,
  Paper,
  Typography,
  Divider,
} from "@mui/material";
import DashboardIcon from "@mui/icons-material/Dashboard";
import AssessmentIcon from "@mui/icons-material/Assessment";
import DescriptionIcon from "@mui/icons-material/Description";
import { useLocation, useNavigate } from "react-router-dom";

const DashboardLayout = memo(() => {
  console.log('✅ DashboardLayout rendered (memo prevents unnecessary re-renders)');
  const location = useLocation();
  const navigate = useNavigate();

  // Determine which tab is active based on the current route
  const getTabValue = () => {
    const pathname = location.pathname;
    if (pathname.includes("analytics")) return 1;
    if (pathname.includes("logs")) return 2;
    return 0;
  };

  const handleTabChange = (event, newValue) => {
    const tabs = ["/summary", "/analytics", "/logs"];
    navigate(tabs[newValue]);
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ mb: 2, fontWeight: 700 }}>
          <DashboardIcon sx={{ mr: 2, verticalAlign: "middle" }} />
          Dashboard
        </Typography>
        <Divider />
      </Box>

      <Paper elevation={2} sx={{ mb: 3 }}>
        <Tabs
          value={getTabValue()}
          onChange={handleTabChange}
          variant="fullWidth"
          indicatorColor="primary"
          textColor="primary"
          sx={{
            backgroundColor: "#f5f5f5",
          }}
        >
          <Tab
            label="Summary"
            icon={<DashboardIcon />}
            iconPosition="start"
            sx={{ textTransform: "none", fontSize: "1rem" }}
          />
          <Tab
            label="Analytics"
            icon={<AssessmentIcon />}
            iconPosition="start"
            sx={{ textTransform: "none", fontSize: "1rem" }}
          />
          <Tab
            label="Logs"
            icon={<DescriptionIcon />}
            iconPosition="start"
            sx={{ textTransform: "none", fontSize: "1rem" }}
          />
        </Tabs>
      </Paper>

      <Box sx={{ mt: 3 }}>
        <Outlet />
      </Box>
    </Container>
  );
});

DashboardLayout.displayName = "DashboardLayout";

export default DashboardLayout;