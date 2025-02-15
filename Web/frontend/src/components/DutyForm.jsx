import { useState } from 'react';

const DutyForm = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    guardId: '',
    location: '',
    shiftStart: '',
    shiftEnd: '',
    description: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      // Validate dates
      const startDate = new Date(formData.shiftStart);
      const endDate = new Date(formData.shiftEnd);

      if (endDate <= startDate) {
        throw new Error("Shift end time must be after start time");
      }

      const success = await onSubmit(formData);
      if (success) {
        setFormData({
          guardId: '',
          location: '',
          shiftStart: '',
          shiftEnd: '',
          description: ''
        });
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md mb-6">
      {error && (
        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block mb-2">Guard ID *</label>
          <input
            type="text"
            name="guardId"
            value={formData.guardId}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-2">Location *</label>
          <input
            type="text"
            name="location"
            value={formData.location}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-2">Shift Start *</label>
          <input
            type="datetime-local"
            name="shiftStart"
            value={formData.shiftStart}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div>
          <label className="block mb-2">Shift End *</label>
          <input
            type="datetime-local"
            name="shiftEnd"
            value={formData.shiftEnd}
            onChange={handleChange}
            className="w-full border rounded p-2"
            required
          />
        </div>

        <div className="col-span-2">
          <label className="block mb-2">Description</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="w-full border rounded p-2"
            rows="3"
          />
        </div>
      </div>

      <button
        type="submit"
        disabled={loading}
        className={`mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 ${
          loading ? 'opacity-50 cursor-not-allowed' : ''
        }`}
      >
        {loading ? 'Assigning...' : 'Assign Duty'}
      </button>
    </form>
  );
};

export default DutyForm;