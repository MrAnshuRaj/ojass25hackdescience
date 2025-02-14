import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "../../utils/apiConnector";

// Async Thunks
export const fetchGuards = createAsyncThunk("guards/fetchGuards", async (_, { rejectWithValue }) => {
  try {
    const response = await axios.get("/guards");
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

export const assignGuard = createAsyncThunk("guards/assignGuard", async ({ guardId, location }, { rejectWithValue }) => {
  try {
    const response = await axios.put(`/guards/assign/${guardId}`, { location });
    return response.data;
  } catch (error) {
    return rejectWithValue(error.response.data);
  }
});

// Slice
const guardsSlice = createSlice({
  name: "guards",
  initialState: {
    guards: [],
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchGuards.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchGuards.fulfilled, (state, action) => {
        state.loading = false;
        state.guards = action.payload;
      })
      .addCase(fetchGuards.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(assignGuard.fulfilled, (state, action) => {
        const index = state.guards.findIndex((guard) => guard.id === action.payload.id);
        if (index !== -1) {
          state.guards[index] = action.payload;
        }
      });
  },
});

export default guardsSlice.reducer;
