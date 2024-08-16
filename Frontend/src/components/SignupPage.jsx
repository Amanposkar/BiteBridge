import React, { useState } from 'react';
import axiosInstance from './Admin/axiosInstance'; // Adjust the path based on your directory structure
import './bootstrap/dist/css/bootstrap.min.css';
import './styles/SignupPage.css';

const SignUpPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [activeForm, setActiveForm] = useState(null); // Track which form is active

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        let endpoint = '';
        switch (activeForm) {
            case 'admin':
                endpoint = '/admin/login';
                break;
            case 'student':
                endpoint = '/student/login';
                break;
            case 'canteen':
                endpoint = '/canteen/login';
                break;
            default:
                return;
        }

        try {
            const response = await axiosInstance.post(endpoint, {
                username,
                password,
            });

            console.log('Login successful:', response.data);

            // Save the JWT token to localStorage or sessionStorage
            localStorage.setItem('jwtToken', response.data.token);

            // Redirect the user to the appropriate page based on the login type
            switch (activeForm) {
                case 'admin':
                    window.location.href = '/';
                    break;
                case 'student':
                    window.location.href = '/student';
                    break;
                case 'canteen':
                    window.location.href = '/canteen';
                    break;
                default:
                    window.location.href = '/';
            }
        } catch (err) {
            if (err.response && err.response.data) {
                setError(err.response.data.message || 'Login failed');
            } else {
                setError('An error occurred. Please try again later.');
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleFormToggle = (form) => {
        setActiveForm(activeForm === form ? null : form);
        setUsername('');
        setPassword('');
        setError(null);
    };

    return (
        <div className="signup-container">
            <div className="signup-buttons">
                <button className="btn btn-primary" onClick={() => handleFormToggle('admin')}>
                    Admin Login
                </button>
                <button className="btn btn-primary" onClick={() => handleFormToggle('student')}>
                    Student Login
                </button>
                <button className="btn btn-primary" onClick={() => handleFormToggle('canteen')}>
                    Canteen Login
                </button>
            </div>

            {activeForm && (
                <div className="signup-form">
                    <h2>{activeForm.charAt(0).toUpperCase() + activeForm.slice(1)} Login</h2>
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="username" className="form-label">Username</label>
                            <input
                                type="text"
                                className="form-control"
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">Password</label>
                            <input
                                type="password"
                                className="form-control"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        {error && <div className="alert alert-danger" role="alert">{error}</div>}
                        <button type="submit" className="col btn btn-pink-moon" disabled={isSubmitting}>
                            {isSubmitting ? `Signing In...` : `Sign In`}
                        </button>
                    </form>
                </div>
            )}
        </div>
    );
};

export default SignUpPage;
