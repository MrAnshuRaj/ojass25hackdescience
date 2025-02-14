import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  guards: [
    { id: "1", name: "John Doe", location: "Gate A", status: "On Duty" },
    { id: "2", name: "Jane Smith", location: "Gate B", status: "Off Duty" },
  ],
  loading: false,
  error: null,
};

const guardsSlice = createSlice({
  name: "guards",
  initialState,
  reducers: {
    addGuard: (state, action) => {
      state.guards.push(action.payload);
    },
    updateGuardStatus: (state, action) => {
      const { id, status } = action.payload;
      const guard = state.guards.find((guard) => guard.id === id);
      if (guard) {
        guard.status = status;
      }
    },
    deleteGuard: (state, action) => {
      state.guards = state.guards.filter((guard) => guard.id !== action.payload);
    },
  },
});

export const { addGuard, updateGuardStatus, deleteGuard } = guardsSlice.actions;

export default guardsSlice.reducer;