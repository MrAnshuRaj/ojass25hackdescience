import { useState } from "react";
import { useDispatch } from "react-redux";
import { reportIncident } from "../store/incidentSlice";

const IncidentReportForm = () => {
  const dispatch = useDispatch();
  const [formData, setFormData] = useState({
    location: "",
    type: "Suspicious Activity",
    description: "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(reportIncident(formData));
    alert("Incident Reported Successfully!");
    setFormData({ location: "", type: "Suspicious Activity", description: "" });
  };

  return (
    <div className="bg-white shadow-md rounded-lg p-6 w-full max-w-lg mx-auto">
      <h2 className="text-2xl font-bold mb-4">Report an Incident</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block font-semibold">Location:</label>
          <input
            type="text"
            name="location"
            value={formData.location}
            onChange={handleChange}
            className="border border-gray-300 p-2 w-full rounded"
            required
          />
        </div>

        <div>
          <label className="block font-semibold">Incident Type:</label>
          <select
            name="type"
            value={formData.type}
            onChange={handleChange}
            className="border border-gray-300 p-2 w-full rounded"
          >
            <option value="Suspicious Activity">Suspicious Activity</option>
            <option value="Unauthorized Access">Unauthorized Access</option>
            <option value="Missing Guard">Missing Guard</option>
            <option value="Theft">Theft</option>
          </select>
        </div>

        <div>
          <label className="block font-semibold">Description:</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="border border-gray-300 p-2 w-full rounded"
            required
          ></textarea>
        </div>

        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Submit Report
        </button>
      </form>
    </div>
  );
};

export default IncidentReportForm;
