import React, { useState } from "react";
import './styles/tokensRecharge.css';
import axiosInstance from '../components/axiosInstance'; // Import axiosInstance

export default function TokensRecharge() {
    // State for the input fields
    const [prnNumber, setPrnNumber] = useState("");
    const [tokens, setTokens] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");

    // Handler function for the form submission
    const handleRecharge = async (e) => {
        e.preventDefault(); // Prevents the default form submission
    
        // Validate PRN Number
        if (prnNumber.length !== 12 || isNaN(prnNumber)) {
            setError("PRN Number must be exactly 12 digits.");
            return;
        }
    
        // Validate Tokens
        if (tokens <= 0) {
            setError("Token amount must be greater than 0.");
            return;
        }
    
        setError("");
        setLoading(true);
    
        try {
            const response = await axiosInstance.patch(`/admin/student/${prnNumber}/${tokens}`);
            console.log("Response Data:", response.data); // Log the response data for debugging
    
            if (response.status === 202) { // Check for 202 status code
                setSuccessMessage("Recharge Successful");
                setPrnNumber("");
                setTokens("");
            } else {
                setError(`Failed to recharge tokens. Status Code: ${response.status}`);
            }
        } catch (error) {
            console.error("Error during token recharge:", error.response ? error.response.data : error.message);
            setError("An error occurred. Please try again later.");
        } finally {
            setLoading(false);
        }
    };
    
    return (
        <div className="recharge-form">
            <form onSubmit={handleRecharge}>
                <div>
                    <label htmlFor="prnNumber">PRN Number:</label>
                    <input
                        type="text"
                        id="prnNumber"
                        value={prnNumber}
                        onChange={(e) => setPrnNumber(e.target.value)}
                        maxLength="12" // Limit to 12 characters
                        placeholder="Enter PRN Number"
                        pattern="\d*" // Allows only numeric input
                        required
                    />
                </div>
                <div>
                    <label htmlFor="tokens">Tokens to Recharge:</label>
                    <input
                        type="number"
                        id="tokens"
                        value={tokens}
                        onChange={(e) => setTokens(e.target.value)}
                        placeholder="Enter number of tokens"
                        min="1" // Ensure at least 1 token is entered
                        required
                    />
                </div>
                {error && <p className="error">{error}</p>}
                {successMessage && <p className="success">{successMessage}</p>}
                <button className="rechargeBtn" type="submit" disabled={loading}>
                    {loading ? "Processing..." : "Recharge"}
                </button>
            </form>
        </div>
    );
}
