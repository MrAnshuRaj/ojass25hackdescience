import { useEffect, useState } from "react";

const IncidentReports = () => {
  const [incidents, setIncidents] = useState([]);

  useEffect(() => {
    const fetchIncidents = async () => {
      const response = await fetch("/api/incidents");
      const data = await response.json();
      setIncidents(data);
    };
    fetchIncidents();
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-3xl font-bold text-red-600">Incident Reports</h2>
      <div className="mt-4">
        {incidents.length > 0 ? (
          <ul>
            {incidents.map((incident) => (
              <li key={incident.id} className="bg-white shadow-md p-4 rounded mb-4">
                <h3 className="text-lg font-semibold">{incident.type}</h3>
                <p className="text-gray-600">{incident.description}</p>
                <p className="text-sm text-gray-500">Location: {incident.location}</p>
                <p className="text-sm text-gray-500">Status: {incident.status}</p>
              </li>
            ))}
          </ul>
        ) : (
          <p>No incidents reported.</p>
        )}
      </div>
    </div>
  );
};

export default IncidentReports;
