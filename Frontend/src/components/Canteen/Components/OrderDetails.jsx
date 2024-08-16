import React, { useEffect, useState } from "react";
import "../styles/orderDetails.css";
import axiosInstance from "../axiosInstance"; // Import the axiosInstance
import { useParams, useNavigate } from "react-router-dom";


const OrderDetails = () => {
  const { orderId } = useParams();
  const [orderData, setOrderData] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchOrderData = async () => {
      try {
        const response = await axiosInstance.get(
          `/canteen/getOrder/${orderId}`
        );
        setOrderData(response.data);
        setError(null);
      } catch (err) {
        console.error(
          "Error fetching order data:",
          err.response ? err.response.data : err.message
        );
        setError("Failed to fetch order data.");
      }
    };

    fetchOrderData();
  }, [orderId]);

  const handleMarkAsServed = async () => {
    try {
      await axiosInstance.delete(`/canteen/served/${orderId}`);
      alert("Order marked as served!");
      navigate("/canteen"); // Redirect back to the QrScanner page
    } catch (err) {
      console.error(
        "Error marking order as served:",
        err.response ? err.response.data : err.message
      );
      setError("Failed to mark order as served.");
    }
  };

  return (
    <div className="order-details">
      <h1>Order Details</h1>
      {error && <div className="error">{error}</div>}
      {orderData ? (
        <div className="order-info">
          <h2>Order ID: {orderData.id}</h2>
          <p>
            <strong>Student Name:</strong> {orderData.studentName}
          </p>
          <p>
            <strong>PRN:</strong> {orderData.prn}
          </p>
          <h3>Dishes:</h3>
          <ul>
            {orderData.dishes.map((dish, index) => (
              <li key={index}>
                <p>
                  <strong>Dish Name:</strong> {dish.dishName}
                </p>
                <p>
                  <strong>Quantity:</strong> {dish.quantity}
                </p>
              </li>
            ))}
          </ul>
          <button className="served-button" onClick={handleMarkAsServed}>
            Served
          </button>
        </div>
      ) : (
        <p>Loading...</p>
      )}
          
    </div>
  );
};

export default OrderDetails;
