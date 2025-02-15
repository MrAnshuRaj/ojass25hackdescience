import apiConnector from "./apiConnector";

export const getAllReports = async () => {
  try {
    const response = await apiConnector.get("/report/");
    return {
      data: Array.isArray(response.data) ? response.data : 
            Array.isArray(response.data?.reports) ? response.data.reports : []
    };
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Failed to fetch reports');
  }
};
export const addReport = (reportData) => apiConnector.post("/report", reportData);
export const deleteReport = (id) => apiConnector.delete(`/report/${id}`);
export const resolveReport = (id) => apiConnector.patch(`/report/${id}/resolve`);
