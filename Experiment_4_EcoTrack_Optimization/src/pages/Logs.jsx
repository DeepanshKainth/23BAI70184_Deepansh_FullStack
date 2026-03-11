import { useEffect, useCallback, memo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchLogs } from "../store/logsSlice";
import {
  Container,
  Box,
  Button,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Card,
  CardContent,
  Typography,
  Chip,
  CircularProgress,
  Alert,
  Divider,
} from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import LeakAddIcon from "@mui/icons-material/LeakAdd";
import NatureIcon from "@mui/icons-material/Nature";

const Logs = memo(() => {
  console.log('✅ Logs rendered (memo prevents unnecessary re-renders)');
  const dispatch = useDispatch();
  const { data: logs, status, error } = useSelector((state) => state.logs);

  // useCallback memoizes the refresh handler to prevent unnecessary re-renders
  const handleRefresh = useCallback(() => {
    console.log('🔄 useCallback: handleRefresh - stable function reference maintained');
    dispatch(fetchLogs());
  }, [dispatch]);

  useEffect(() => {
    if (status === "idle") {
      dispatch(fetchLogs());
    }
  }, [status, dispatch]);

  if (status === "loading") {
    return (
      <Container maxWidth="lg" sx={{ py: 5, display: "flex", justifyContent: "center" }}>
        <Box sx={{ textAlign: "center" }}>
          <CircularProgress />
          <Typography variant="body1" sx={{ mt: 2 }}>
            Loading activity logs...
          </Typography>
        </Box>
      </Container>
    );
  }

  if (status === "failed") {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          Error: {error}
        </Alert>
        <Button
          variant="contained"
          color="primary"
          onClick={handleRefresh}
          startIcon={<RefreshIcon />}
        >
          Try Again
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 3 }}>
      <Box sx={{ mb: 4 }}>
        <Typography
          variant="h5"
          component="h2"
          gutterBottom
          sx={{ fontWeight: 600, mb: 2 }}
        >
          Activity Logs
        </Typography>
        <Button
          variant="contained"
          color="primary"
          onClick={handleRefresh}
          startIcon={<RefreshIcon />}
          sx={{
            textTransform: "none",
            fontSize: "1rem",
            backgroundColor: "#2e7d32",
            "&:hover": {
              backgroundColor: "#1b5e20",
            },
          }}
        >
          Refresh Logs
        </Button>
      </Box>

      {logs.length === 0 ? (
        <Alert severity="info">No activity logs available. Click Refresh to load data.</Alert>
      ) : (
        <Card elevation={3}>
          <CardContent>
            <Typography variant="subtitle2" color="textSecondary" sx={{ mb: 2 }}>
              Total Activities: {logs.length}
            </Typography>
            <Divider sx={{ mb: 2 }} />
            <List>
              {logs.map((log, index) => (
                <Box key={log.id}>
                  <ListItem
                    sx={{
                      backgroundColor: index % 2 === 0 ? "#fafafa" : "white",
                      borderRadius: 1,
                      mb: 1,
                      "&:hover": {
                        backgroundColor: "#f0f0f0",
                      },
                    }}
                  >
                    <ListItemIcon>
                      {log.carbon >= 4 ? (
                        <LeakAddIcon sx={{ color: "#d32f2f", fontSize: 28 }} />
                      ) : (
                        <NatureIcon sx={{ color: "#2e7d32", fontSize: 28 }} />
                      )}
                    </ListItemIcon>
                    <ListItemText
                      primary={log.activity}
                      secondary={`Carbon Emission: ${log.carbon} kg CO₂`}
                      primaryTypographyProps={{ fontWeight: 600 }}
                    />
                    <Box sx={{ display: "flex", gap: 1 }}>
                      <Chip
                        label={`${log.carbon} kg`}
                        sx={{
                          backgroundColor: log.carbon >= 4 ? "#ffcdd2" : "#c8e6c9",
                          fontWeight: 600,
                        }}
                      />
                      <Chip
                        label={log.carbon >= 4 ? "High Impact" : "Low Impact"}
                        color={log.carbon >= 4 ? "error" : "success"}
                        variant="outlined"
                        size="small"
                      />
                    </Box>
                  </ListItem>
                </Box>
              ))}
            </List>
          </CardContent>
        </Card>
      )}
    </Container>
  );
});

Logs.displayName = "Logs";

export default Logs;

