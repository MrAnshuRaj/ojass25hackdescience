import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { assignGuard } from "../redux/slices/guardsSlice";

const GuardCard = ({ guard }) => {
  const dispatch = useDispatch();
  const [location, setLocation] = useState("");

  const handleAssign = () => {
    if (location) {
      dispatch(assignGuard({ guardId: guard.id, location }));
      setLocation("");
    }
  };

  return (
    <div className="border p-4 rounded-lg shadow-lg">
      <h2 className="text-xl font-semibold">{guard.name}</h2>
      <p>Experience: {guard.experience} years</p>
      <p>Current Location: {guard.location || "Not assigned"}</p>
      <input
        type="text"
        value={location}
        onChange={(e) => setLocation(e.target.value)}
        placeholder="Assign Location"
        className="border p-2 rounded mt-2 w-full"
      />
      <button
        onClick={handleAssign}
        className="bg-blue-500 text-white px-4 py-2 rounded mt-2"
      >
        Assign
      </button>
    </div>
  );
};

export default GuardCard;
