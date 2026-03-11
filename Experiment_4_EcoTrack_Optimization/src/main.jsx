import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { AuthProvider } from "./context/AuthContext";
import { Provider } from "react-redux";
import store from "./store/store";
import { ThemeProvider, createTheme, CssBaseline } from "@mui/material";

// Create a custom Material UI theme
const theme = createTheme({
  palette: {
    primary: {
      main: "#2e7d32",
      light: "#66bb6a",
      dark: "#1b5e20",
    },
    secondary: {
      main: "#1976d2",
      light: "#42a5f5",
      dark: "#1565c0",
    },
    error: {
      main: "#d32f2f",
      light: "#e57373",
      dark: "#c62828",
    },
    success: {
      main: "#2e7d32",
      light: "#66bb6a",
      dark: "#1b5e20",
    },
    warning: {
      main: "#f57c00",
    },
    background: {
      default: "#fafafa",
      paper: "#ffffff",
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 700,
      fontSize: "2.125rem",
    },
    h5: {
      fontWeight: 600,
      fontSize: "1.5rem",
    },
    h6: {
      fontWeight: 600,
      fontSize: "1.25rem",
    },
    subtitle1: {
      fontWeight: 500,
      fontSize: "1rem",
    },
    button: {
      textTransform: "none",
      fontWeight: 500,
    },
  },
  shape: {
    borderRadius: 8,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: "none",
          fontWeight: 600,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          transition: "all 0.3s ease",
          "&:hover": {
            boxShadow: "0 8px 16px rgba(0,0,0,0.1)",
          },
        },
      },
    },
  },
});

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Provider store={store}>
        <AuthProvider>
          <App />
        </AuthProvider>
      </Provider>
    </ThemeProvider>
  </StrictMode>,
);
