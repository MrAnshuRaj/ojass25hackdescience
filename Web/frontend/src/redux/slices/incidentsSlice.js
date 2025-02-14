import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  incidents: [],
  loading: false,
  error: null,
};

const incidentsSlice = createSlice({
  name: "incidents",
  initialState,
  reducers: {
    reportIncident: (state, action) => {
      state.incidents.push(action.payload);
    },
    resolveIncident: (state, action) => {
      const incident = state.incidents.find(
        (incident) => incident.id === action.payload
      );
      if (incident) {
        incident.status = "Resolved";
      }
    },
    deleteIncident: (state, action) => {
      state.incidents = state.incidents.filter(
        (incident) => incident.id !== action.payload
      );
    },
  },
});

export const { reportIncident, resolveIncident, deleteIncident } =
  incidentsSlice.actions;

export default incidentsSlice.reducer;