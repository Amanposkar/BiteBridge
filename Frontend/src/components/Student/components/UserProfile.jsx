// src/components/UserProfile.js
import React from 'react';
import '../styles/userProfile.css'; // Optional: Create a CSS file for styling

const UserProfile = ({ userName, tokenBalance }) => {
    return (
        <div className="user-profile">
            <h3>Name: {userName}</h3>
            <p>Token Balance: {tokenBalance}</p>
        </div>
    );
};

export default UserProfile;
