import { useState } from 'react';

const IncidentForm = ({ onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    guard: '',
    description: '',
    status: 'Pending'
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
    setFormData({ guard: '', description: '', status: 'Pending' });
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
      <h3 className="text-lg font-semibold mb-4">Report New Incident</h3>
      
      <div className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Guard ID</label>
          <input
            type="text"
            value={formData.guard}
            onChange={(e) => setFormData({...formData, guard: e.target.value})}
            className="mt-1 w-full border rounded-md p-2"
            required
          />
        </div>
        
        <div>
          <label className="block text-sm font-medium text-gray-700">Description</label>
          <textarea
            value={formData.description}
            onChange={(e) => setFormData({...formData, description: e.target.value})}
            className="mt-1 w-full border rounded-md p-2"
            rows="3"
            required
          />
        </div>
      </div>

      <div className="mt-4 flex justify-end space-x-2">
        <button
          type="button"
          onClick={onCancel}
          className="px-4 py-2 text-gray-600 hover:text-gray-800"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          Submit Report
        </button>
      </div>
    </form>
  );
};

export default IncidentForm;