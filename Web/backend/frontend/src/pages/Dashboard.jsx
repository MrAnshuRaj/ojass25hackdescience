import { Link } from "react-router-dom";
import { useSelector } from "react-redux";

const Dashboard = () => {
  const { user } = useSelector((state) => state.auth);

  return (
    <div className="p-6">
      <h2 className="text-3xl font-bold text-blue-900">Dashboard</h2>
      <p className="text-gray-600">Welcome, {user?.name || "User"}!</p>

      <div className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
        <Link to="/guard-list" className="bg-blue-500 text-white p-4 rounded-lg">
          Manage Guards
        </Link>
        <Link to="/incident-reports" className="bg-red-500 text-white p-4 rounded-lg">
          View Incidents
        </Link>
        <Link to="/track-guards" className="bg-green-500 text-white p-4 rounded-lg">
          Live Tracking
        </Link>
      </div>
    </div>
  );
};

export default Dashboard;
