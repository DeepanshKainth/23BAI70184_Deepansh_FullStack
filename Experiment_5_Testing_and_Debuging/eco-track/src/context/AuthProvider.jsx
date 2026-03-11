import AuthContext from "./AuthContext" 
import { useState } from "react"
import React from "react"

const AuthProvider = ({children}) => {
    const [user, setUser] = useState(() => {
        return localStorage.getItem("token") ? true : null;
    });

    const login = (token) => {
        localStorage.setItem("token", token) ;
        setUser(true) ;
    } 
    const logout = () => {
        localStorage.removeItem("token") ;
        setUser(null) ; 
    }

    return (
        <AuthContext.Provider value = {{user, login, logout}}> 
            {children}
        </AuthContext.Provider>
    )
}
export default AuthProvider ;