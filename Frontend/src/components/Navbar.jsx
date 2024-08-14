import React, { useState } from "react";
import axiosInstance from './axiosInstance'; // Import axiosInstance
import Tokens from './TokensRecharge';
import Students from './StudentsAccount';
import Menu from './ItemMenu';
import './styles/navbar.css';

export default function Navbar() {
    const [isOpen, setIsOpen] = useState(false); // Controls dropdown menu visibility
    const [selectedItem, setSelectedItem] = useState(null);
    const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    const handleMenuItemClick = (item) => {
        setSelectedItem(item);
        // Optionally, you can keep the dropdown menu open or not
        // setIsOpen(false); // Uncomment if you want to close the dropdown after selection
    };

    const toggleProfileDropdown = () => {
        setProfileDropdownOpen(!profileDropdownOpen);
    };

    const handleLogout = async () => {
        try {
            await axiosInstance.post('/admin/logout'); // Make a POST request to the logout endpoint
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
            <div className="navbarCover">
                <div className="AdminNav-items">
                    <ul>
                        <li className="hamburger" onClick={toggleMenu}>
                            <span className="line"></span>
                            <span className="line"></span>
                            <span className="line"></span>
                        </li>
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
                <div className={`AdminDropdown-menu ${isOpen ? 'open' : ''}`}>
                    <ul>
                        <li onClick={() => handleMenuItemClick("Update Menu")}>Update Menu</li>
                        <li onClick={() => handleMenuItemClick("Students Account")}>Students Account</li>
                        <li onClick={() => handleMenuItemClick("Tokens Recharge")}>Tokens Recharge</li>
                    </ul>
                </div>
            </div>
            <div className="content-area">
                {/* Display different content based on selected item */}
                {selectedItem === "Update Menu" && <Menu></Menu>}
                {selectedItem === "Students Account" && <Students />}
                {selectedItem === "Tokens Recharge" && <Tokens />}
                {!selectedItem && <h1>Welcome!</h1>}
            </div>
        </div>
    );
}
