import React, { useState } from "react";
import axiosInstance from './axiosInstance'; // Adjust the path based on your directory structure
import "./Login.css";

export default function Login() {
    const [userId, setUserId] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Replace '/admin/login' with your actual login endpoint
            const response = await axiosInstance.post('/admin/login', {
                username: userId,
                password: password,
            });
            
            // Assuming the JWT token is returned in response.data.token
            localStorage.setItem('jwtToken', response.data.token);
            
            // Redirect or perform other actions on successful login
            window.location.href = '/'; // Redirect to home page or dashboard
        } catch (error) {
            console.error('Login error:', error);
            // Handle error (e.g., show a message to the user)
        }
    };

    return (
        <div>
            <div className="mainFormDiv">
                <form onSubmit={handleSubmit}>
                    <table>
                        <tbody>
                            <tr>
                                <td>
                                    <label htmlFor="id">Enter User-ID:</label>
                                </td>
                                <td>
                                    <input
                                        type="text"
                                        name="id"
                                        value={userId}
                                        onChange={(e) => setUserId(e.target.value)}
                                        placeholder="240340120001"
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label htmlFor="pwd">Enter Password:</label>
                                </td>
                                <td>
                                    <input
                                        type="password"
                                        name="pwd"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                    />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <br />
                    <button type="submit">Login</button>
                </form>
            </div>
        </div>
    );
}
