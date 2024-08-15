import React, { useState, useEffect } from "react";
import axios from "axios";

const OrdersList = () => {
  const [orders, setOrders] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    // Fetch all orders
    axios.get("/api/orders") // Update with your API endpoint
      .then(response => {
        if (Array.isArray(response.data)) {
          setOrders(response.data);
        } else {
          setMessage(response.data.message);
        }
      })
      .catch(error => {
        console.error("Error fetching orders:", error);
      });
  }, []);

  return (
    <div>
      <h2>Your Orders</h2>
      {message && <p>{message}</p>}
      {orders.length > 0 ? (
        <div>
          {orders.map((order, index) => (
            <div key={index}>
              <h4>Order ID: {order.orderId}</h4>
              <p>Status: {order.orderStatus}</p>
              <p>Total: {order.totalCartPrice}</p>
            </div>
          ))}
        </div>
      ) : (
        <p>No active orders found.</p>
      )}
    </div>
  );
};

export default OrdersList;
