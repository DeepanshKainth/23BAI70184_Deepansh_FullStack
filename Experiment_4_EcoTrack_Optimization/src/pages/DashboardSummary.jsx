import { memo, useMemo } from "react";
import { useSelector } from "react-redux";
import {
  Card,
  CardContent,
  Typography,
  Grid,
  Box,
  LinearProgress,
  Paper,
} from "@mui/material";
import TrendingDownIcon from "@mui/icons-material/TrendingDown";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";

const DashboardSummary = memo(() => {
  console.log('✅ DashboardSummary rendered (memo prevents unnecessary re-renders)');
  const logs = useSelector((state) => state.logs.data);

  // useMemo memoizes the calculation of total carbon footprint
  // to avoid redundant calculations on every render
  const totalCarbonFootprint = useMemo(() => {
    console.log('📊 useMemo: Calculating totalCarbonFootprint');
    return logs.reduce((acc, log) => acc + log.carbon, 0);
  }, [logs]);

  // useMemo for calculating average carbon per activity
  const averageCarbon = useMemo(() => {
    console.log('📊 useMemo: Calculating averageCarbon');
    if (logs.length === 0) return 0;
    return (totalCarbonFootprint / logs.length).toFixed(2);
  }, [totalCarbonFootprint, logs.length]);

  // useMemo for identifying high-carbon activities
  const highCarbonActivities = useMemo(() => {
    console.log('📊 useMemo: Calculating highCarbonActivities count');
    return logs.filter((log) => log.carbon >= 4).length;
  }, [logs]);

  // useMemo for identifying low-carbon activities
  const lowCarbonActivities = useMemo(() => {
    console.log('📊 useMemo: Calculating lowCarbonActivities count');
    return logs.filter((log) => log.carbon < 4).length;
  }, [logs]);

  return (
    <Box sx={{ py: 3 }}>
      <Typography variant="h5" component="h2" gutterBottom sx={{ mb: 3, fontWeight: 600 }}>
        Summary Overview
      </Typography>

      <Grid container spacing={3}>
        {/* Total Carbon Footprint Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card elevation={3} sx={{ backgroundColor: "#fff3e0" }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <TrendingUpIcon sx={{ color: "#d32f2f", mr: 1 }} />
                <Typography color="textSecondary" gutterBottom>
                  Total Carbon
                </Typography>
              </Box>
              <Typography variant="h4" sx={{ fontWeight: 700, color: "#d32f2f" }}>
                {totalCarbonFootprint} kg
              </Typography>
              <Typography variant="caption" color="textSecondary">
                Total footprint
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Average Carbon Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card elevation={3} sx={{ backgroundColor: "#e3f2fd" }}>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
                Average Carbon
              </Typography>
              <Typography variant="h4" sx={{ fontWeight: 700, color: "#1976d2" }}>
                {averageCarbon} kg
              </Typography>
              <Typography variant="caption" color="textSecondary">
                Per activity
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* High Carbon Activities Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card elevation={3} sx={{ backgroundColor: "#ffebee" }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <TrendingUpIcon sx={{ color: "#c62828", mr: 1 }} />
                <Typography color="textSecondary" gutterBottom>
                  High Carbon
                </Typography>
              </Box>
              <Typography variant="h4" sx={{ fontWeight: 700, color: "#c62828" }}>
                {highCarbonActivities}
              </Typography>
              <Typography variant="caption" color="textSecondary">
                Activities ≥ 4kg
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Low Carbon Activities Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card elevation={3} sx={{ backgroundColor: "#e8f5e9" }}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <TrendingDownIcon sx={{ color: "#2e7d32", mr: 1 }} />
                <Typography color="textSecondary" gutterBottom>
                  Low Carbon
                </Typography>
              </Box>
              <Typography variant="h4" sx={{ fontWeight: 700, color: "#2e7d32" }}>
                {lowCarbonActivities}
              </Typography>
              <Typography variant="caption" color="textSecondary">
                Activities &lt; 4kg
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Carbon Progress Indicator */}
      <Paper elevation={2} sx={{ mt: 4, p: 3, backgroundColor: "#f5f5f5" }}>
        <Typography variant="h6" gutterBottom sx={{ mb: 2, fontWeight: 600 }}>
          Carbon Footprint Progress
        </Typography>
        <Typography variant="body2" color="textSecondary" sx={{ mb: 2 }}>
          Total: {totalCarbonFootprint} kg out of 100 kg target
        </Typography>
        <LinearProgress
          variant="determinate"
          value={Math.min((totalCarbonFootprint / 100) * 100, 100)}
          sx={{
            height: 10,
            borderRadius: 5,
            backgroundColor: "#e0e0e0",
            "& .MuiLinearProgress-bar": {
              backgroundColor: totalCarbonFootprint > 50 ? "#d32f2f" : "#2e7d32",
            },
          }}
        />
      </Paper>
    </Box>
  );
});

DashboardSummary.displayName = "DashboardSummary";

export default DashboardSummary;