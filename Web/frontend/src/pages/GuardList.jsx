import React from "react";
import { useSelector } from "react-redux";
import GuardCard from "../components/guardCard";

const GuardList = () => {
  const { guards } = useSelector((state) => state.guards);

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold text-orange-600 mb-4">Security Guards</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {guards.map((guard) => (
          <GuardCard key={guard.id} guard={guard} />
        ))}
      </div>
    </div>
  );
};

export default GuardList;