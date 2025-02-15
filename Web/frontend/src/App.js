import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Provider, useSelector } from "react-redux";
import store from "./redux/store"; 
import Navbar from "./components/Navbar";
import Sidebar from "./components/Sidebar";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Guards from "./pages/Guards";
import Duties from "./pages/Duties";
import Reports from "./pages/Reports";
import "./App.css";
import "./index.css";

function App() {
  return (
    <Provider store={store}>
      <Router>
        <MainLayout />
      </Router>
    </Provider>
  );
}

function MainLayout() {
  const user = useSelector((state) => state.auth.user);

  return (
    <div className="flex">
      {user && <Sidebar />}
      <div className="flex-1">
        <Navbar />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/guards"
            element={
              <ProtectedRoute>
                <Guards />
              </ProtectedRoute>
            }
          />
          <Route
            path="/duties"
            element={
              <ProtectedRoute>
                <Duties />
              </ProtectedRoute>
            }
          />
          <Route
            path="/reports"
            element={
              <ProtectedRoute>
                <Reports />
              </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </div>
  );
}

export default App;
