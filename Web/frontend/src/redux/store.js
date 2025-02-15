import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import guardReducer from "./guardSlice";
import dutyReducer from "./dutySlice";
import reportReducer from "./reportSlice";

const store = configureStore({
  reducer: {
    auth: authReducer,
    guards: guardReducer,
    duties: dutyReducer,
    reports: reportReducer,
  },
});

export default store;
