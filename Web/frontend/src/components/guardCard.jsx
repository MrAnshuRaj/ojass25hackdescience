import React from "react";

const GuardCard = ({ guard }) => {
  return (
    <div className="bg-gray-100 border-l-4 border-orange-600 p-4 rounded-lg shadow-md w-80">
      <h2 className="text-lg font-bold text-gray-800">{guard.name}</h2>
      <p className="text-gray-600">ID: {guard.id}</p>
      <p className="text-gray-600">Location: {guard.location}</p>
      <p className="text-gray-600">Status: {guard.status}</p>
    </div>
  );
};

export default GuardCard;