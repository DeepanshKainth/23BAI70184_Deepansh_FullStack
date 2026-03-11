import React, { useState, useEffect, useCallback } from "react";
import { Container, Typography, Button, Box } from "@mui/material";
import fx from "fireworks";

const CounterDisplay = React.memo(() => {
  const [count, setCount] = useState(() => {
    const saved = localStorage.getItem("waterCount");
    return saved ? Number(saved) : 0;
  });

  const goal = 8;

  const handleAdd = useCallback(() => {
    setCount((prev) => prev + 1);
  }, []);

  const handleRemove = useCallback(() => {
    setCount((prev) => (prev > 0 ? prev - 1 : 0));
  }, []);

  const handleReset = useCallback(() => {
    setCount(0);
  }, []);

  useEffect(() => {
    localStorage.setItem("waterCount", count);
  }, [count]);

  useEffect(() => {
    if (count >= goal) {
      let i = 0;
      const interval = setInterval(() => {
        fx({
          x: Math.random() * window.innerWidth,
          y: Math.random() * (window.innerHeight / 2),
        });
        i++;
        if (i === 8) clearInterval(interval);
      }, 400);
      return () => clearInterval(interval);
    }
  }, [count]);

  return (
    <Container maxWidth="sm" sx={{ textAlign: "center", mt: 5 }}>
      <Typography variant="h3" gutterBottom>
        Water Tracker
      </Typography>

      <Typography variant="h6">
        {count} / {goal} Glasses
      </Typography>

      <Box sx={{ mt: 3 }}>
        <Button variant="contained" onClick={handleAdd} sx={{ mr: 1 }}>
          + Add
        </Button>

        <Button variant="outlined" onClick={handleRemove} sx={{ mr: 1 }}>
          - Remove
        </Button>

        <Button variant="contained" color="error" onClick={handleReset}>
          Reset
        </Button>
      </Box>

      {count >= goal && (
        <Typography
          sx={{ mt: 3, color: "green", fontFamily: "cursive" }}
          variant="h4"
        >
          Goal Reached!
        </Typography>
      )}
    </Container>
  );
});

export default CounterDisplay;