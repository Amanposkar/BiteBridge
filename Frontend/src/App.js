import './App.css';
import { Routes, Route } from 'react-router-dom';
import Signup from './components/SignupPage.jsx';
import StudentHome from './components/Student/components/Homepage.jsx';
import AdminHome from './components/AdminHome.jsx';
import DishList from './components/DishList.jsx';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path='/signup' element={<Signup />} />
        {/* Protecting routes */}
        <Route path='/' element={<ProtectedRoute><AdminHome /></ProtectedRoute>} />
        <Route path='/student' element={<ProtectedRoute><StudentHome /></ProtectedRoute>} />
        <Route path='/dishes' element={<ProtectedRoute><DishList /></ProtectedRoute>} />
      </Routes>
    </div>
  );
}

export default App;
