import React from "react";
import IncidentReportForm from "../components/incidentReportForm";

const IncidentReports = () => {
  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold text-orange-600 mb-4">Incident Reports</h1>
      <div className="flex justify-center">
        <IncidentReportForm />
      </div>
    </div>
  );
};

export default IncidentReports;