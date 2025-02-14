import { configureStore } from "@reduxjs/toolkit";
import guardsReducer from "./slices/guardsSlice";
import incidentsReducer from "./slices/incidentsSlice";

const store = configureStore({
  reducer: {
    guards: guardsReducer,
    incidents: incidentsReducer,
  },
});

export default store;