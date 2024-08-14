import React, { useState } from "react";
import '../styles/studentNavbar.css';
import Home from './HomeMenu';

export default function NavbarStudent() {
    const [isOpen, setIsOpen] = useState(false); // Controls dropdown menu visibility
    const [selectedItem, setSelectedItem] = useState("Home");
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

    const handleLogout = () => {
        alert("Logging out...");
        // Implement logout logic here
    };

    return (
        <div className="mainContainer">
            <div className="navbarCover">
                <div className="nav-items">
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
                <div className={`studentDropdown-menu ${isOpen ? 'open' : ''}`}>
                    <ul>
                        <li onClick={() => handleMenuItemClick("Home")}>Home</li>
                        <li onClick={() => handleMenuItemClick("FriendList")}>FriendList</li>
                        <li onClick={() => handleMenuItemClick("Cart")}>Cart</li>
                    </ul>
                </div>
            </div>
            <div className="content-area">
                {/* Display different content based on selected item */}
                {selectedItem === "Home" && <Home />}
                {selectedItem === "FriendList" && <p>Friend List</p>}
                {selectedItem === "Cart" && <p>Cart Page</p>}
                {!selectedItem && <h1>Welcome!</h1>}
            </div>
        </div>
    );
}
