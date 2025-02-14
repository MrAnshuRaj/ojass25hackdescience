import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div className="text-center p-8">
      <h1 className="text-4xl font-bold text-orange-600 mb-4">
        Welcome to Security Management System
      </h1>
      <p className="text-gray-600 mb-8">
        Manage security guards, track their locations, and report incidents efficiently.
      </p>
      <div className="flex justify-center gap-4">
        <Link
          to="/dashboard"
          className="bg-orange-600 text-white px-6 py-2 rounded-lg hover:bg-orange-700"
        >
          Go to Dashboard
        </Link>
        <Link
          to="/guards"
          className="bg-gray-600 text-white px-6 py-2 rounded-lg hover:bg-gray-700"
        >
          View Guards
        </Link>
      </div>
    </div>
  );
};

export default Home;