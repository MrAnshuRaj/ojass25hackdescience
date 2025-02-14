import React from "react";
import GuardCard from "../components/guardCard";

const Dashboard = () => {
  // Static data for demonstration
  const guards = [
    { id: "1", name: "John Doe", location: "Gate A", status: "On Duty" },
    { id: "2", name: "Jane Smith", location: "Gate B", status: "Off Duty" },
  ];

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold text-orange-600 mb-4">Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {guards.map((guard) => (
          <GuardCard key={guard.id} guard={guard} />
        ))}
      </div>
    </div>
  );
};

export default Dashboard;