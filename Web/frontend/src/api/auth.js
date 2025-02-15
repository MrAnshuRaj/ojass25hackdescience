import apiConnector from "./apiConnector";


export const login = async (credentials) => {
  return apiConnector.post("/auth/login", credentials);
};


export const register = async (userData) => {
  return apiConnector.post("/auth/register", userData);
};

export const getAllUsers = async () => {
  return apiConnector.get("/auth/");
}


// export const logout = async () => {
//   return apiConnector.post("/auth/logout");
// };

// export const getUserProfile = async () => {
//   return apiConnector.get("/auth/profile");
// };
