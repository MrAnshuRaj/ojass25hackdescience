import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="text-center">
        <h1 className="text-4xl font-bold text-blue-900">Security Management System</h1>
        <p className="mt-4 text-lg text-gray-600">
          Real-time security guard tracking, incident reporting, and efficient deployment.
        </p>
        <div className="mt-6 flex space-x-4">
          <Link to="/login" className="bg-blue-500 text-white px-4 py-2 rounded">
            Login
          </Link>
          <Link to="/signup" className="bg-gray-500 text-white px-4 py-2 rounded">
            Sign Up
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Home;
