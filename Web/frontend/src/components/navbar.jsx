import React from "react";
import { Link } from "react-router-dom";
import logo from "../assets/Icon.png";

const Navbar = () => {
    return (
        <nav className="bg-orange-600 text-gray-200 p-2 flex justify-between items-center">
            <div className="flex items-center gap-2 pl-5">
                <img src={logo} alt="Track My Guard Logo" className="h-12" />
                <h1 className="text-x1 font-bold text-white">TRACK MY GUARD</h1>
            </div>
            <ul className="flex space-x-6 pr-10">
                {["Home", "Dashboard", "Guards", "Incidents"].map((item, index) => (
                    <li key={index} className="relative group">
                        <Link to={`/${item.toLowerCase()}`} className="text-white font-medium relative transition-all duration-300 ease-in-out transform group-hover:scale-105">
                            {item}
                            <span className="absolute left-1/2 bottom-[-4px] w-0 h-[2px] bg-white transition-all duration-300 ease-in-out group-hover:w-full group-hover:left-0"></span>
                        </Link>
                    </li>
                ))}
            </ul>
        </nav>
    );
};

export default Navbar;
