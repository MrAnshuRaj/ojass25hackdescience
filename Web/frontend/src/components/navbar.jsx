import { Link, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../redux/authSlice";
import logo from "../asset/icon.png";

const Navbar = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user } = useSelector((state) => state.auth);

  const handleLogout = () => {
    dispatch(logout());
    navigate("/login");
  };

  return (
    <nav className="bg-orange-600 p-4 text-white shadow-lg">
      <div className="flex justify-between items-center max-w-7xl mx-auto">
        <Link to="/dashboard" className="flex items-center space-x-2">
          <img src={logo} alt="shield" className="w-8 h-8" />
          <h1 className="text-2xl font-bold">Track My Guard</h1>
        </Link>
        <div className="flex items-center gap-6">
          {!user ? (
            <>
              <Link 
                to="/login" 
                className="hover:bg-orange-500 px-4 py-2 rounded-lg transition-colors"
              >
                Login
              </Link>
              <Link 
                to="/register" 
                className="bg-orange-700 hover:bg-orange-800 px-4 py-2 rounded-lg transition-colors"
              >
                Register
              </Link>
            </>
          ) : (
            <>
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 bg-orange-700 rounded-full flex items-center justify-center">
                  {user.name?.charAt(0).toUpperCase()}
                </div>
                <span className="text-white font-medium">
                  Welcome, {user.name}
                </span>
              </div>
              <button 
                onClick={handleLogout} 
                className="bg-orange-700 hover:bg-orange-800 px-4 py-2 rounded-lg transition-colors flex items-center gap-2"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
