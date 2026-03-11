import React from "react" ;
import { NavLink } from "react-router-dom";

const NavLinks = () => {
        return (
            <ul className="flex justify-between items-center gap-6 ">
                <li>
                    <NavLink to = "/home" 
                        className={({ isActive }) =>
                        `pb-2 transition-all ${
                            isActive
                            ? "border-b-2 border-blue-600"
                            : ""
                        }`}>Home
                    </NavLink>
                </li>
                <li>
                    <NavLink to = ""
                        className={({ isActive }) =>
                        `pb-2 transition-all ${
                            isActive
                            ? "border-b-2 border-blue-600"
                            : ""
                        }`}>About
                    </NavLink>
                </li>
                <li>
                    <NavLink to = "/logs"
                        className={({ isActive }) =>
                        `pb-2 transition-all ${
                            isActive
                            ? "border-b-2 border-blue-600"
                            : ""
                        }`}
                        >Logs
                    </NavLink>
                </li>
                <li>
                    <NavLink to = ""
                        className={({ isActive }) =>
                            `pb-2 transition-all ${
                                isActive
                                ? " border-b-2 border-blue-600"
                                : ""
                            }`}
                            >Feedback
                    </NavLink>
                </li>
            </ul>
        )
    }

    export default NavLinks ;