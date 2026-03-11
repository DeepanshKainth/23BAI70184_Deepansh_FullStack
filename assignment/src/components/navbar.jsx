import { Box, Button, Typography } from "@mui/material";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/authContext";

export default function Navbar({ title }) {
  const {handleLogout} = useContext(AuthContext); 
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        width: "100%",
        p: 2,
      }}
    >
      <Typography
        variant="h3"
        sx={{
          mb: 2,
          fontFamily: "emoji",
        }}
      >
        {title}
      </Typography>

      <Box sx={{ display: "flex", gap: 3, fontSize: 20, color:"white", textDecoration: 'none' }}>
        <Link to="/dashboard">Home</Link>
        <Link to="/dashboard/water">Water</Link>
        <Button variant="outlined" onClick={()=>{
          handleLogout(); 
        }}>Logout</Button>
      </Box>
    </Box>
  );
}