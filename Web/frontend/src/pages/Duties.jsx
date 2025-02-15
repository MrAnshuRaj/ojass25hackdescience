import { useEffect, useState } from "react";
import { getAllDuties, deleteDuty, addDuty } from "../api/duty";
import DutyForm from "../components/DutyForm";

const Duties = () => {
  const [duties, setDuties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchDuties = async () => {
    try {
      setLoading(true);
      const response = await getAllDuties();
      console.log(response.data);
      setDuties(Array.isArray(response.data) ? response.data : []);
      setError(null);
    } catch (err) {
      setError(err.message || "Failed to fetch duties");
      setDuties([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDuties();
  }, []);

  const handleDutyAdded = () => {
    fetchDuties(); // Refresh the duties list
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this duty?')) {
      try {
        await deleteDuty(id);
        fetchDuties();
      } catch (err) {
        setError(err.message || "Failed to delete duty");
      }
    }
  };

  const handleSubmitDuty = async (dutyData) => {
    console.log(dutyData);
    try {
      setLoading(true);
      await addDuty({
        guardId: dutyData.guardId,
        location: dutyData.location,
        shiftStart: dutyData.shiftStart,
        shiftEnd: dutyData.shiftEnd,
        description: dutyData.description
      });
      await fetchDuties();
      return true;
    } catch (err) {
      setError(err.message || "Failed to add duty");
      return false;
    } finally {
      setLoading(false);
    }
  };

  const formatDateTime = (dateString) => {
    try {
      return new Date(dateString).toLocaleString();
    } catch (err) {
      return 'Invalid date';
    }
  };

  const formatGuardDisplay = (guard) => {
    console.log('guard' , guard);
    if (!guard) return 'No guard assigned';
    return guard.name || 'Unnamed guard';
  };

  if (loading) return <div className="p-6">Loading duties...</div>;
  if (error) return <div className="p-6 text-red-500">{error}</div>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-6">Duty Management</h2>
      
      <DutyForm onSubmit={handleSubmitDuty} />

      <div className="mt-8">
        {duties.length === 0 ? (
          <p className="text-gray-500">No duties assigned.</p>
        ) : (
          <div className="grid gap-4">
            {duties.map((duty) => (
              <div 
                key={duty._id} 
                className="bg-white rounded-lg shadow p-4"
              >
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-gray-600">Guard Name</p>
                    <p className="font-medium">{formatGuardDisplay(duty.guard)}</p>
                  </div>
                  <div>
                    <p className="text-gray-600">Location</p>
                    <p className="font-medium">{typeof duty.location === 'object' ? duty.location.name : duty.location}</p>
                  </div>
                  <div>
                    <p className="text-gray-600">Shift Start</p>
                    <p className="font-medium">{formatDateTime(duty.shiftStart)}</p>
                  </div>
                  <div>
                    <p className="text-gray-600">Shift End</p>
                    <p className="font-medium">{formatDateTime(duty.shiftEnd)}</p>
                  </div>
                  <div>
                    <p className="text-gray-600">Status</p>
                    <p className={`font-medium ${
                      duty.status === 'Assigned' ? 'text-green-600' : 'text-gray-600'
                    }`}>
                      {duty.status}
                    </p>
                  </div>
                  <div>
                    <p className="text-gray-600">Assigned By</p>
                    <p className="font-medium">
                      {typeof duty.assignedBy === 'object' ? duty.assignedBy.name : duty.assignedBy}
                    </p>
                  </div>
                </div>
                <div className="mt-4 flex justify-end">
                  <button
                    onClick={() => handleDelete(duty._id)}
                    className="px-3 py-1 text-red-600 hover:text-red-800"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Duties;
