import React, { useState } from 'react';
import axiosInstance from './axiosInstance'; // Adjust the path based on your directory structure
import './bootstrap/dist/css/bootstrap.min.css';
import './styles/SignupPage.css';

const SignUpPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError(null);

        try {
            const response = await axiosInstance.post('http://localhost:8080/admin/login', {
                username,
                password,
            });

            console.log('Login successful:', response.data);

            // Save the JWT token to localStorage or sessionStorage
            localStorage.setItem('jwtToken', response.data.token);

            // Redirect the user or handle post-login state here
            window.location.href = '/';
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

    return (
        <div className="signup-container">
            <div className="icon-container">
                <img src={`${process.env.PUBLIC_URL}/images/burger-icon.png`} alt="Sign Up Icon" />
            </div>
            <div className="signup-form">
                <h2>Sign Up</h2>
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
                        {isSubmitting ? 'Signing Up...' : 'Sign Up'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default SignUpPage;
