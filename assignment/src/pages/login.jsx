import { Box, Button, Typography } from "@mui/material";
import { useContext } from "react";
import { AuthContext } from "../context/authContext";

export default function LoginPage() {
  const { handleLogin } = useContext(AuthContext);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <Typography variant="h4" mb={2}>
        Hello
      </Typography>

      <Button variant="contained" onClick={()=>handleLogin()}>
        Login Now
      </Button>
    </Box>
  );
}