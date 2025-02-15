import { useEffect, useState } from "react";
import { getAllReports, addReport, resolveReport } from "../api/reports";
import IncidentForm from "../components/IncidentForm";

const Reports = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [filter, setFilter] = useState('all'); // 'all', 'pending', 'resolved'

  const fetchReports = async () => {
    try {
      setLoading(true);
      const response = await getAllReports();
      // Ensure we're setting an array of reports
      const reportsData = response.data?.reports || response.data || [];
      setReports(Array.isArray(reportsData) ? reportsData : []);
      setError(null);
    } catch (err) {
      setError(err.message || "Failed to fetch reports");
      setReports([]); // Ensure reports is an empty array on error
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReports();
  }, []);

  const handleSubmitReport = async (formData) => {
    try {
      await addReport(formData);
      await fetchReports();
      setShowForm(false);
    } catch (err) {
      setError(err.message || "Failed to create report");
    }
  };

  const handleResolveReport = async (reportId) => {
    try {
      await resolveReport(reportId);
      await fetchReports();
    } catch (err) {
      setError(err.message || "Failed to resolve report");
    }
  };

  const filteredReports = reports.filter(report => {
    if (filter === 'pending') return report.status === 'Pending';
    if (filter === 'resolved') return report.status === 'Resolved';
    return true;
  });

  if (loading) return <div className="p-6">Loading reports...</div>;
  if (error) return <div className="p-6 text-red-500">{error}</div>;

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">Incident Reports</h2>
        <button
          onClick={() => setShowForm(true)}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
        >
          Report Incident
        </button>
      </div>

      <div className="mb-4 flex space-x-2">
        <button
          onClick={() => setFilter('all')}
          className={`px-3 py-1 rounded ${
            filter === 'all' ? 'bg-gray-200' : 'bg-gray-100'
          }`}
        >
          All
        </button>
        <button
          onClick={() => setFilter('pending')}
          className={`px-3 py-1 rounded ${
            filter === 'pending' ? 'bg-yellow-200' : 'bg-yellow-100'
          }`}
        >
          Pending
        </button>
        <button
          onClick={() => setFilter('resolved')}
          className={`px-3 py-1 rounded ${
            filter === 'resolved' ? 'bg-green-200' : 'bg-green-100'
          }`}
        >
          Resolved
        </button>
      </div>

      {showForm && (
        <div className="mb-6">
          <IncidentForm
            onSubmit={handleSubmitReport}
            onCancel={() => setShowForm(false)}
          />
        </div>
      )}

      <div className="grid gap-4">
        {filteredReports.map((report) => (
          <div 
            key={report._id} 
            className="bg-white rounded-lg shadow p-4"
          >
            <div className="flex justify-between items-start mb-2">
              <div>
                <p className="text-gray-600 mb-2">{report.description}</p>
                <span className={`px-2 py-1 text-sm rounded ${
                  report.status === 'Resolved' ? 'bg-green-100 text-green-800' :
                  report.status === 'Pending' ? 'bg-yellow-100 text-yellow-800' :
                  'bg-red-100 text-red-800'
                }`}>
                  {report.status}
                </span>
              </div>
              <p className="text-sm text-gray-500">
                {new Date(report.createdAt).toLocaleString()}
              </p>
            </div>
            
            <div className="mt-4 grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-gray-500">Report ID</p>
                <p className="font-mono">{report._id}</p>
              </div>
              <div>
                <p className="text-gray-500">Reported By</p>
                <p>{report.reportedBy?.name || report.reportedBy || 'Anonymous'}</p>
              </div>
              <div>
                <p className="text-gray-500">Guard</p>
                <p>{report.guard?.name || 'Not specified'}</p>
              </div>
              <div>
                <p className="text-gray-500">Last Updated</p>
                <p>{new Date(report.updatedAt).toLocaleString()}</p>
              </div>
            </div>

            {report.status === 'Pending' && (
              <div className="mt-4 border-t pt-4">
                <button
                  onClick={() => handleResolveReport(report._id)}
                  className="px-3 py-1 text-green-600 hover:text-green-800"
                >
                  Mark as Resolved
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Reports;
