import React from "react";
import bgImage from "../assets/home_bg.jpg";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div 
      className="w-full h-screen flex flex-col items-center justify-center text-center"
      style={{
        backgroundImage: `url(${bgImage})`,
        backgroundColor: "#f4f4f4",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat"
      }}
    >
      <div className="bg-white bg-opacity-80 p-8 rounded-lg shadow-md max-w-lg">
        <h1 className="text-4xl font-bold text-orange-600 mb-4">
          Welcome to Security Management System
        </h1>
        <p className="text-gray-700 mb-6 font-bold">
          Manage security guards, track their locations, and report incidents efficiently.
        </p>
        <div className="flex flex-col sm:flex-row justify-center gap-4">
          <Link
            to="/dashboard"
            className="bg-orange-600 text-white px-6 py-2 rounded-lg hover:bg-orange-700 transition duration-300"
          >
            Go to Dashboard
          </Link>
          <Link
            to="/guards"
            className="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700 transition duration-300"
          >
            View Guards
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Home;
