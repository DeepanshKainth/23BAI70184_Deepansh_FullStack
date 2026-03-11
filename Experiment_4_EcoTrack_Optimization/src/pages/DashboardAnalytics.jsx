import { memo, useMemo } from "react";
import { useSelector } from "react-redux";
import {
  Box,
  Card,
  CardContent,
  Typography,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Chip,
  Grid,
  Paper,
} from "@mui/material";
import LeakAddIcon from "@mui/icons-material/LeakAdd";
import NatureIcon from "@mui/icons-material/Nature";
import TimelineIcon from "@mui/icons-material/Timeline";

const DashboardAnalytics = memo(() => {
  console.log('✅ DashboardAnalytics rendered (memo prevents unnecessary re-renders)');
  const logs = useSelector((state) => state.logs.data);

  // useMemo to calculate high carbon activities with memoization
  const highCarbonActivities = useMemo(() => {
    console.log('📊 useMemo: Filtering highCarbonActivities');
    return logs.filter((log) => log.carbon >= 4);
  }, [logs]);

  // useMemo to calculate low carbon activities
  const lowCarbonActivities = useMemo(() => {
    console.log('📊 useMemo: Filtering lowCarbonActivities');
    return logs.filter((log) => log.carbon < 4);
  }, [logs]);

  // useMemo to sort activities by carbon content
  const sortedByCarbon = useMemo(() => {
    console.log('📊 useMemo: Sorting activities by carbon');
    return [...logs].sort((a, b) => b.carbon - a.carbon);
  }, [logs]);

  // useMemo to calculate carbon distribution
  const carbonDistribution = useMemo(() => {
    return {
      highPercent: logs.length > 0 ? ((highCarbonActivities.length / logs.length) * 100).toFixed(1) : 0,
      lowPercent: logs.length > 0 ? ((lowCarbonActivities.length / logs.length) * 100).toFixed(1) : 0,
    };
  }, [logs, highCarbonActivities, lowCarbonActivities]);

  return (
    <Box sx={{ py: 3 }}>
      <Typography variant="h5" component="h2" gutterBottom sx={{ mb: 3, fontWeight: 600 }}>
        <TimelineIcon sx={{ mr: 2, verticalAlign: "middle" }} />
        Analytics Dashboard
      </Typography>

      <Grid container spacing={3}>
        {/* High Carbon Activities */}
        <Grid item xs={12} md={6}>
          <Card elevation={3}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <LeakAddIcon sx={{ color: "#d32f2f", mr: 1, fontSize: 28 }} />
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  High Carbon Activities
                </Typography>
                <Chip
                  label={`${carbonDistribution.highPercent}%`}
                  color="error"
                  size="small"
                  sx={{ ml: "auto" }}
                />
              </Box>
              <List>
                {highCarbonActivities.length > 0 ? (
                  highCarbonActivities.map((log) => (
                    <ListItem key={log.id} sx={{ pb: 1 }}>
                      <ListItemIcon>
                        <LeakAddIcon sx={{ color: "#d32f2f" }} />
                      </ListItemIcon>
                      <ListItemText
                        primary={log.activity}
                        secondary={`${log.carbon} kg CO₂`}
                      />
                      <Chip label="High" color="error" variant="outlined" size="small" />
                    </ListItem>
                  ))
                ) : (
                  <Typography variant="body2" color="textSecondary">
                    No high carbon activities
                  </Typography>
                )}
              </List>
            </CardContent>
          </Card>
        </Grid>

        {/* Low Carbon Activities */}
        <Grid item xs={12} md={6}>
          <Card elevation={3}>
            <CardContent>
              <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                <NatureIcon sx={{ color: "#2e7d32", mr: 1, fontSize: 28 }} />
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Low Carbon Activities
                </Typography>
                <Chip
                  label={`${carbonDistribution.lowPercent}%`}
                  color="success"
                  size="small"
                  sx={{ ml: "auto" }}
                />
              </Box>
              <List>
                {lowCarbonActivities.length > 0 ? (
                  lowCarbonActivities.map((log) => (
                    <ListItem key={log.id} sx={{ pb: 1 }}>
                      <ListItemIcon>
                        <NatureIcon sx={{ color: "#2e7d32" }} />
                      </ListItemIcon>
                      <ListItemText
                        primary={log.activity}
                        secondary={`${log.carbon} kg CO₂`}
                      />
                      <Chip label="Low" color="success" variant="outlined" size="small" />
                    </ListItem>
                  ))
                ) : (
                  <Typography variant="body2" color="textSecondary">
                    No low carbon activities
                  </Typography>
                )}
              </List>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Activities Ranked by Carbon Footprint */}
      <Paper elevation={2} sx={{ mt: 3, p: 3 }}>
        <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
          Activities Ranked by Carbon Emission
        </Typography>
        <List>
          {sortedByCarbon.length > 0 ? (
            sortedByCarbon.map((log, index) => (
              <ListItem
                key={log.id}
                sx={{
                  backgroundColor: index % 2 === 0 ? "#fafafa" : "white",
                  mb: 1,
                  borderRadius: 1,
                }}
              >
                <ListItemText
                  primary={`${index + 1}. ${log.activity}`}
                  secondary={`Carbon Emission: ${log.carbon} kg CO₂`}
                  primaryTypographyProps={{ fontWeight: 600 }}
                />
                <Box
                  sx={{
                    width: 80,
                    height: 40,
                    backgroundColor: log.carbon >= 4 ? "#ffcdd2" : "#c8e6c9",
                    borderRadius: 1,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <Typography variant="subtitle2" sx={{ fontWeight: 700 }}>
                    {log.carbon}kg
                  </Typography>
                </Box>
              </ListItem>
            ))
          ) : (
            <Typography variant="body2" color="textSecondary">
              No data available
            </Typography>
          )}
        </List>
      </Paper>
    </Box>
  );
});

DashboardAnalytics.displayName = "DashboardAnalytics";

export default DashboardAnalytics;