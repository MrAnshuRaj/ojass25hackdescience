import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchGuards } from "../redux/slices/guardsSlice";
import GuardCard from "../components/GuardCard";

const GuardList = () => {
  const dispatch = useDispatch();
  const { guards, loading, error } = useSelector((state) => state.guards);

  useEffect(() => {
    dispatch(fetchGuards());
  }, [dispatch]);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Security Guards</h1>
      {loading && <p>Loading...</p>}
      {error && <p className="text-red-500">{error}</p>}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {guards.map((guard) => (
          <GuardCard key={guard.id} guard={guard} />
        ))}
      </div>
    </div>
  );
};

export default GuardList;
