import { memo, useCallback } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  Alert,
  Paper,
} from "@mui/material";
import LoginIcon from "@mui/icons-material/Login";

const Login = memo(() => {
  console.log('✅ Login rendered (memo prevents unnecessary re-renders)');
  const { setIsAuthenticated } = useAuth();
  const navigate = useNavigate();

  // useCallback memoizes the login handler
  const handleLogin = useCallback(() => {
    console.log('🔄 useCallback: handleLogin - stable function reference maintained');
    setIsAuthenticated(true);
    navigate("/");
  }, [setIsAuthenticated, navigate]);

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
          <CardContent>
            <Typography
              variant="h4"
              component="h1"
              gutterBottom
              sx={{
                textAlign: "center",
                fontWeight: 700,
                color: "#2e7d32",
                mb: 3,
              }}
            >
              EcoTrack
            </Typography>

            <Typography
              variant="subtitle1"
              sx={{
                textAlign: "center",
                mb: 3,
                color: "textSecondary",
              }}
            >
              Track Your Carbon Footprint
            </Typography>

            <Alert severity="info" sx={{ mb: 3 }}>
              Demo Login: Click the button below to access your dashboard
            </Alert>

            <TextField
              fullWidth
              label="Username"
              variant="outlined"
              placeholder="Enter username"
              disabled
              defaultValue="demo_user"
              sx={{ mb: 2 }}
            />

            <TextField
              fullWidth
              label="Password"
              type="password"
              variant="outlined"
              placeholder="Enter password"
              disabled
              defaultValue="••••••••"
              sx={{ mb: 3 }}
            />

            <Button
              fullWidth
              variant="contained"
              color="primary"
              onClick={handleLogin}
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
              Login to Dashboard
            </Button>

            <Paper elevation={0} sx={{ mt: 3, p: 2, backgroundColor: "#f5f5f5" }}>
              <Typography variant="caption" display="block" sx={{ mb: 1 }}>
                Demo Credentials:
              </Typography>
              <Typography variant="caption" color="textSecondary">
                This is a demo application. Click the login button to access your
                environmental dashboard.
              </Typography>
            </Paper>
          </CardContent>
        </Card>
      </Box>
    </Container>
  );
});

Login.displayName = "Login";

export default Login;