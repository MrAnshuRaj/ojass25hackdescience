import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import apiConnector from "../api/apiConnector";

export const fetchDuties = createAsyncThunk("duties/fetchAll", async (_, { rejectWithValue }) => {
  try {
    const response = await apiConnector.get("/duty/");
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

const dutySlice = createSlice({
  name: "duties",
  initialState: { duties: [], loading: false, error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchDuties.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchDuties.fulfilled, (state, action) => {
        state.duties = action.payload;
        state.loading = false;
      })
      .addCase(fetchDuties.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      });
  },
});

export default dutySlice.reducer;
