import React from "react";
import { Outlet } from "react-router-dom";
import Header from "../components/Header";

const Layout = () => {
    return (
        <section className="w-full bg-gradient-to-r from-blue-50 to-indigo-50 pt-4">
            <Header/>
            <Outlet/>
        </section>
    )
}
export default Layout ;