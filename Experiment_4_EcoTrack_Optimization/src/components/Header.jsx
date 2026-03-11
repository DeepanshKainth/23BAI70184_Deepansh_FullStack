import { memo, useCallback } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
} from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";

const Header = memo(({ title }) => {
  console.log('✅ Header rendered (memo prevents unnecessary re-renders)');
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  // useCallback memoizes the logout handler to prevent unnecessary re-renders
  const handleLogout = useCallback(() => {
    console.log('🔄 useCallback: handleLogout - stable function reference maintained');
    navigate("/logout");
  }, [navigate]);

  return (
    <AppBar position="static" sx={{ backgroundColor: "#2e7d32", mb: 3 }}>
      <Container maxWidth="lg">
        <Toolbar disableGutters>
          <Typography
            variant="h5"
            component="div"
            sx={{
              flexGrow: 1,
              fontWeight: 700,
              letterSpacing: 1.5,
              color: "white",
            }}
          >
            {title || "EcoTrack"}
          </Typography>
          {isAuthenticated && (
            <Button
              color="inherit"
              onClick={handleLogout}
              startIcon={<LogoutIcon />}
              sx={{
                backgroundColor: "#c62828",
                padding: "8px 16px",
                borderRadius: "4px",
                textTransform: "none",
                fontSize: "1rem",
                "&:hover": {
                  backgroundColor: "#b71c1c",
                },
              }}
            >
              Logout
            </Button>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
});

Header.displayName = "Header";

export default Header;
