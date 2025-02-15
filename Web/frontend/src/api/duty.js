import apiConnector from "./apiConnector";

export const getAllDuties = async () => {
  try {
    const response = await apiConnector.get("/duty");
    console.log(response.data);
    return {
      data: Array.isArray(response.data) ? response.data : 
            Array.isArray(response.data?.duties) ? response.data.duties : []
    };
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Failed to fetch duties');
  }
};
export const addDuty = async (dutyData) => {
  console.log('dutydata' , dutyData);
  try {
    const transformedData = {
      guard: dutyData.guardId,
      location: dutyData.location,
      shiftStart: new Date(dutyData.shiftStart).toISOString(),
      shiftEnd: new Date(dutyData.shiftEnd).toISOString(),
      description: dutyData.description || '',
      status: 'Assigned'
    };
    console.log(transformedData);

    const response = await apiConnector.post("/duty", transformedData);
    return response;
  } catch (error) {
    const errorMessage = error.response?.data?.message || error.message;
    throw new Error(errorMessage);
  }
};
export const deleteDuty = (id) => apiConnector.delete(`/duty/${id}`);
