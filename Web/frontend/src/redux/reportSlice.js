import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import apiConnector from "../api/apiConnector";

export const fetchReports = createAsyncThunk("reports/fetchAll", async (_, { rejectWithValue }) => {
  try {
    const response = await apiConnector.get("/report");
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

const reportSlice = createSlice({
  name: "reports",
  initialState: { reports: [], loading: false, error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchReports.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchReports.fulfilled, (state, action) => {
        state.reports = action.payload;
        state.loading = false;
      })
      .addCase(fetchReports.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      });
  },
});

export default reportSlice.reducer;
