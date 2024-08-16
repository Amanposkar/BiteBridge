import React, { useEffect, useState } from "react";
import axiosInstance from "../axiosInstance"; // Adjust the import path based on your project structure
import '../styles/OrdersTable.css';

const OrdersTable = () => {
  const [orders, setOrders] = useState([]);
  const [message, setMessage] = useState(null);
  const [timestamp, setTimestamp] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function fetchOrders() {
      try {
        const response = await axiosInstance.get("/student/orders");
        // Check if response contains orders array or message
        if (Array.isArray(response.data)) {
          setOrders(response.data);
        } else if (response.data.message) {
          setMessage(response.data.message);
          setTimestamp(response.data.timestamp);
        } else {
          setError("Unexpected response format.");
        }
      } catch (err) {
        console.error("Error fetching orders:", err);
        setError("Failed to fetch orders.");
      }
    }

    fetchOrders();
  }, []);

  return (
    <div>
      <h1>Orders</h1>
      {error && <div style={{ color: "red" }}>{error}</div>}
      {message ? (
        <div>
          <p>{message}</p>
          {timestamp && <p>Timestamp: {new Date(timestamp).toLocaleString()}</p>}
        </div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Status</th>
              <th>Timestamp</th>
              <th>Total Cart Price</th>
              <th>Items</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.orderStatus}</td>
                <td>{new Date(order.timeStamp).toLocaleString()}</td>
                <td>{order.totalCartPrice}</td>
                <td>
                  <ul>
                    {order.cartItemDTO.map((item, index) => (
                      <li key={index}>
                        {item.dishName} - {item.quantity} x ₹{item.price} = ₹
                        {item.totalPrice}
                      </li>
                    ))}
                  </ul>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default OrdersTable;
