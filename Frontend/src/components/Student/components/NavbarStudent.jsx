import React, { useState, useEffect } from "react";
import axiosInstance from '../axiosInstance';
import '../styles/studentNavbar.css';
import Friends from './FriendList';
import Home from './HomeMenu';
import Cart from "./Cart";
import UserProfile from './UserProfile'; // Import the new component
import OrdersTable from './OrdersTable'; // Import the OrdersTable component
import OrderHistory from './OrderHistory';

export default function NavbarStudent() {
    const [isOpen, setIsOpen] = useState(false);
    const [selectedItem, setSelectedItem] = useState("Home");
    const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);
    const [cartItems, setCartItems] = useState([]); // State to hold cart items
    const [orderSelection, setOrderSelection] = useState([]); // State to hold order selection
    const [tokenBalance, setTokenBalance] = useState(0); // State to hold token balance
    const [userName, setUserName] = useState(""); // State to hold user name

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    const handleMenuItemClick = (item) => {
        setSelectedItem(item);
    };

    const toggleProfileDropdown = () => {
        setProfileDropdownOpen(!profileDropdownOpen);
    };

    const handleLogout = async () => {
        try {
            await axiosInstance.post('/student/logout');
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('cartItems'); // Clear cart items on logout
            alert("You have been logged out.");
            window.location.href = '/signup';
        } catch (error) {
            console.error('Error logging out:', error);
            alert("Error logging out. Please try again.");
        }
    };

    const handleViewCart = (updatedCartItems) => {
        setCartItems(updatedCartItems); // Update the cartItems state
        setSelectedItem("Cart");
    };

    const resetOrderSelection = () => {
        setOrderSelection([]); // Clear order selection
    };

    const fetchUserProfile = async () => {
        try {
            const response = await axiosInstance.get('/student/studentdetails'); // Update endpoint as needed
            setUserName(response.data.first_name);
            setTokenBalance(response.data.tokens_balance);
        } catch (error) {
            console.error('Error fetching user profile:', error);
        }
    };

    const updateTokenBalance = async () => {
        await fetchUserProfile(); // Update token balance after order
    };

    useEffect(() => {
        fetchUserProfile(); // Fetch user profile and token balance on component mount
    }, []);

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
                        <li onClick={() => handleMenuItemClick("Your Orders")}>Your Orders</li> {/* Added new menu item */}
                        <li onClick={() => handleMenuItemClick("Order History")}>Order History</li>
                    </ul>
                </div>
            </div>
            <div className="content-area">
                <UserProfile userName={userName} tokenBalance={tokenBalance} /> {/* Pass userName and tokenBalance to UserProfile */}
                {selectedItem === "Home" && (
                    <Home 
                        onViewCart={handleViewCart} 
                        cartItems={cartItems} // Pass current cartItems to Home
                        setCartItems={setCartItems} // Pass setCartItems to update cartItems from Home
                    />
                )}
                {selectedItem === "FriendList" && <Friends />}
                {selectedItem === "Cart" && (
                    <Cart 
                        setCartItems={setCartItems} // Pass setCartItems to Cart
                        resetOrderSelection={resetOrderSelection} // Pass resetOrderSelection to Cart
                        updateTokenBalance={updateTokenBalance} // Pass updateTokenBalance to Cart
                    />
                )}
                {selectedItem === "Your Orders" && <OrdersTable />} {/* Display OrdersTable component */}
                {selectedItem === "Order History" && <OrderHistory />}
                {!selectedItem && <h1>Welcome!</h1>}
            </div>
        </div>
    );
}
