import React from "react";
import { Routes, Route } from "react-router-dom";
import Navbar from "./components/navbar";
import Home from "./pages/Home";
import Dashboard from "./pages/Dashboard";
import GuardList from "./pages/GuardList";
import IncidentReports from "./pages/IncidentReports";
import NotFound from "./pages/NotFound";
import "./App.css";
import "./index.css";

const App = () => {
  return (
    <div>
      <Navbar />
      <main className="p-4">
        <Routes>
          <Route path="/home" element={<Home />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/guards" element={<GuardList />} />
          <Route path="/incidents" element={<IncidentReports />} />
          <Route path="*" element={<NotFound />} /> {/* Handle invalid routes */}
        </Routes>
      </main>
    </div>
  );
};

export default App;