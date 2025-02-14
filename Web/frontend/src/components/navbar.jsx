import React from "react";
import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <nav className="bg-orange-600 p-4 text-gray-200 flex justify-between items-center">
      <h1 className="text-xl font-bold">Security Management</h1>
      <ul className="flex space-x-4">
        <li>
          <Link to="/" className="hover:underline">Home</Link>
        </li>
        <li>
          <Link to="/dashboard" className="hover:underline">Dashboard</Link>
        </li>
        <li>
          <Link to="/guards" className="hover:underline">Guards</Link>
        </li>
        <li>
          <Link to="/incidents" className="hover:underline">Incidents</Link>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
