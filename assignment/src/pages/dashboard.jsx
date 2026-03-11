import { Box, Typography } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { data, Outlet } from 'react-router-dom'
import Navbar from '../components/navbar'

export default function Dashboard() {
  const [message, setMessage] = useState(""); 
  
  useEffect(()=>{
    fetch("https://api.adviceslip.com/advice").then(res => res.json()).then(data=> setMessage(data.slip.advice)).catch(error=> console.log(error)); 
  },[]); 


  return (
    <>
    <Navbar title="Dashboard" />
    {message !== "" && <Typography sx={{fontFamily: 'cursive', textAlign: 'center'}} variant='h5'>Today's Healthy Tip: {message}</Typography>}
    <Outlet />
    </>
  )
}
