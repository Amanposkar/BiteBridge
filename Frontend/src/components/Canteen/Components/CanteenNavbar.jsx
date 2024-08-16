import React, { useState } from "react";
import axiosInstance from '../axiosInstance'; // Import axiosInstance
import QrScanner from './QrScanner';
import '../styles/CanteenNavbar.css';

export default function CanteenNavbar() {
    const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);

    const toggleProfileDropdown = () => {
        setProfileDropdownOpen(!profileDropdownOpen);
    };

    const handleLogout = async () => {
        try {
            await axiosInstance.post('/canteen/logout'); // Make a POST request to the logout endpoint
            localStorage.removeItem('jwtToken'); // Remove token from local storage
            alert("You have been logged out.");
            window.location.href = '/signup'; // Redirect to login page or home page
        } catch (error) {
            console.error('Error logging out:', error);
            alert("Error logging out. Please try again.");
        }
    };

    return (
        <div className="mainContainer">
            <div className="canteenNavbarCover">
                <div className="CanteenNav-items">
                    <ul>
                        <li>ByteBridge</li>
                    </ul>
                    <ul>
                        <li className="profileImage" onClick={toggleProfileDropdown}>
                            <img src="/images/profileIcon.png" alt="ProfileIcon" />
                            {/* Profile dropdown */}
                            <div className={`profile-dropdown ${profileDropdownOpen ? 'open' : ''}`}>
                                <ul>
                                    <li onClick={handleLogout}>Logout</li>
                                </ul>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div className="canteenContent-area">
                <QrScanner />
            </div>
        </div>
    );
}
