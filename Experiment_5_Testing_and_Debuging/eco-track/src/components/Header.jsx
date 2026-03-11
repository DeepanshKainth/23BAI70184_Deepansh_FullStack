import React, { useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import useAuth from "../hooks/useAuth";

import NavLinks from "./NavLinks";


const Header = () => {
    const { login, logout, user } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const isLoggingIn = useRef(false);

    const isLogin = Boolean(user);

    useEffect(() => {
        if (user && isLoggingIn.current && location.pathname === "/login") {
            isLoggingIn.current = false;
            navigate("/home", { replace: true });
        }
    }, [user, navigate, location.pathname]);

    const handleSubmit = () => {
        debugger; // For debugging
        if (isLogin) {
            logout();
            navigate("/login", { replace: true });
        } else {
            isLoggingIn.current = true;
            login("real-jwt-token");
        }
    };

    

    return (
        <div className="flex items-center justify-between mx-auto h-14 w-full max-w-6xl px-4 sm:px-6 md:px-10 shadow-lg rounded-full bg-white">
            <div className="flex items-center gap-3">
                <h1 className="text-xl font-semibold text-gray-700">Eco-Track</h1>
            </div>
            {isLogin && <NavLinks/> }
            <button onClick={handleSubmit} className = "cursor-pointer">
                {isLogin ? "Logout" : "Login"}
            </button>
        </div>
    );
};

export default Header;
