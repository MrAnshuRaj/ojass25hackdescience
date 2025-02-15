import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import apiConnector from "../api/apiConnector";


const setAuthToken = (token) => {
  if (token) {
    localStorage.setItem("token", token);
    apiConnector.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    localStorage.removeItem("token");
    delete apiConnector.defaults.headers.common["Authorization"];
  }
};


const getInitialState = () => {
  const token = localStorage.getItem("token");
  if (token) {
    apiConnector.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    return {
      token,
      user: null,
      loading: false,
      error: null,
      isAuthenticated: true
    };
  }
  return {
    token: null,
    user: null,
    loading: false,
    error: null,
    isAuthenticated: false
  };
};

export const login = createAsyncThunk(
  "auth/login",
  async (credentials, { rejectWithValue }) => {
    try {
      const response = await apiConnector.post("/auth/login", credentials);
      setAuthToken(response.data.token);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data || "Login failed");
    }
  }
);

export const register = createAsyncThunk(
  "auth/register",
  async (userData, { rejectWithValue }) => {
    try {
      const response = await apiConnector.post("/auth/register", userData);
      setAuthToken(response.data.token);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response?.data || "Registration failed");
    }
  }
);

export const logout = createAsyncThunk(
  "auth/logout",
  async (_, { rejectWithValue }) => {
    try {
      await apiConnector.post("/auth/logout");
      setAuthToken(null);
      return null;
    } catch (error) {
      return rejectWithValue(error.response?.data || "Logout failed");
    }
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState: getInitialState(),
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    resetAuth: (state) => {
      setAuthToken(null);
      state.token = null;
      state.user = null;
      state.loading = false;
      state.error = null;
      state.isAuthenticated = false;
    }
  },
  extraReducers: (builder) => {
    builder

      .addCase(login.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.user;
        state.token = action.payload.token;
        state.isAuthenticated = true;
        state.error = null;
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
        state.isAuthenticated = false;
      })

      .addCase(register.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(register.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.user;
        state.token = action.payload.token;
        state.isAuthenticated = true;
        state.error = null;
      })
      .addCase(register.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
        state.isAuthenticated = false;
      })
  
      .addCase(logout.fulfilled, (state) => {
        state.user = null;
        state.token = null;
        state.isAuthenticated = false;
        state.error = null;
      })
      .addCase(logout.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export const { clearError, resetAuth } = authSlice.actions;
export default authSlice.reducer;
