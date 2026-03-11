import { useEffect, memo } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import {
  Container,
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Alert,
} from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import LoginIcon from "@mui/icons-material/Login";

const Logout = memo(() => {
  console.log('✅ Logout rendered (memo prevents unnecessary re-renders)');
  const { setIsAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    setIsAuthenticated(false);
  }, [setIsAuthenticated]);

  return (
    <Container maxWidth="sm">
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "60vh",
        }}
      >
        <Card elevation={4} sx={{ width: "100%", maxWidth: 400 }}>
          <CardContent sx={{ textAlign: "center" }}>
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                mb: 2,
              }}
            >
              <CheckCircleIcon
                sx={{
                  fontSize: 60,
                  color: "#2e7d32",
                }}
              />
            </Box>

            <Typography
              variant="h5"
              component="h1"
              gutterBottom
              sx={{
                fontWeight: 700,
                color: "#2e7d32",
                mb: 2,
              }}
            >
              Logout Successful
            </Typography>

            <Alert severity="success" sx={{ mb: 3 }}>
              You have been logged out successfully
            </Alert>

            <Typography
              variant="body1"
              color="textSecondary"
              sx={{ mb: 3 }}
            >
              Thank you for using EcoTrack! We hope you've gained valuable insights
              into your carbon footprint.
            </Typography>

            <Button
              fullWidth
              variant="contained"
              color="primary"
              component={RouterLink}
              to="/login"
              startIcon={<LoginIcon />}
              size="large"
              sx={{
                py: 1.5,
                fontSize: "1rem",
                fontWeight: 600,
                backgroundColor: "#2e7d32",
                "&:hover": {
                  backgroundColor: "#1b5e20",
                },
              }}
            >
              Return to Login
            </Button>
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
});

Logout.displayName = "Logout";

export default Logout;
