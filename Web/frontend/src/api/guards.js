import apiConnector from "./apiConnector";

export const getAllGuards = async () => {
  try {
    const response = await apiConnector.get("/guards/");
    return {
      data: Array.isArray(response.data) ? response.data : 
            Array.isArray(response.data?.guards) ? response.data.guards : []
    };
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Failed to fetch guards');
  }
};
export const addGuard = (guardData) => apiConnector.post("/guards", guardData);
export const deleteGuard = (id) => apiConnector.delete(`/guards/${id}`);
