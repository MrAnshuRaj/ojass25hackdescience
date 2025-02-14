import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./slices/authSlice";
import guardsReducer from "./slices/guardsSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
    guards: guardsReducer,
  },
});

export default store;
