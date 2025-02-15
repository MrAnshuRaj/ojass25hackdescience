import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import apiConnector from "../api/apiConnector";

export const fetchGuards = createAsyncThunk("guards/fetchAll", async (_, { rejectWithValue }) => {
  try {
    const response = await apiConnector.get("/guards");
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

const guardSlice = createSlice({
  name: "guards",
  initialState: { guards: [], loading: false, error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchGuards.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchGuards.fulfilled, (state, action) => {
        state.guards = action.payload;
        state.loading = false;
      })
      .addCase(fetchGuards.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      });
  },
});

export default guardSlice.reducer;
