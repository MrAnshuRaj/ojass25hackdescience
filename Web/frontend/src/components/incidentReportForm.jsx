import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { reportIncident } from "../redux/slices/incidentsSlice";

const IncidentReportForm = () => {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    location: "",
  });

  const dispatch = useDispatch();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const incident = {
      id: Date.now().toString(),
      ...formData,
      status: "Pending",
    };
    dispatch(reportIncident(incident));
    setFormData({ title: "", description: "", location: "" });
  };

  return (
    <form onSubmit={handleSubmit} className="bg-gray-100 p-6 rounded-lg shadow-md w-96">
      <h2 className="text-lg font-bold text-orange-600 mb-4">Report an Incident</h2>
      <label className="block mb-2 text-gray-700">Title</label>
      <input
        type="text"
        name="title"
        value={formData.title}
        onChange={handleChange}
        className="w-full p-2 border border-gray-300 rounded"
        required
      />
      <label className="block mt-4 mb-2 text-gray-700">Description</label>
      <textarea
        name="description"
        value={formData.description}
        onChange={handleChange}
        className="w-full p-2 border border-gray-300 rounded"
        required
      ></textarea>
      <label className="block mt-4 mb-2 text-gray-700">Location</label>
      <input
        type="text"
        name="location"
        value={formData.location}
        onChange={handleChange}
        className="w-full p-2 border border-gray-300 rounded"
        required
      />
      <button
        type="submit"
        className="mt-4 w-full bg-orange-600 text-white p-2 rounded hover:bg-orange-700"
      >
        Submit Report
      </button>
    </form>
  );
};

export default IncidentReportForm;