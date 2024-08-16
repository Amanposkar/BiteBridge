import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance'; // Adjust the import path as needed
// import '../styles/OrderHistory.css'; // Create a CSS file for styling if needed

const OrderHistory = () => {
    const [orders, setOrders] = useState([]);
    const [message, setMessage] = useState(null);
    const [timestamp, setTimestamp] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Fetch the order history data from the backend
        const fetchOrderHistory = async () => {
            try {
                const response = await axiosInstance.get('/student/history'); // Adjust endpoint as needed

                // Check if response contains orders array or a message object
                if (Array.isArray(response.data)) {
                    setOrders(response.data);
                } else if (response.data.message) {
                    setMessage(response.data.message);
                    setTimestamp(response.data.timestamp);
                } else {
                    setError("Unexpected response format.");
                }
            } catch (error) {
                console.error('Error fetching order history:', error);
                setError('Failed to fetch order history.');
            }
        };

        fetchOrderHistory();
    }, []);

    return (
        <div className="order-history">
            <h2>Order History</h2>
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
                            <th>Time</th>
                            <th>Total Price</th>
                            <th>Items</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map(order => (
                            <tr key={order.orderId}>
                                <td>{order.orderId}</td>
                                <td>{order.orderStatus}</td>
                                <td>{new Date(order.timeStamp).toLocaleString()}</td>
                                <td>{order.totalCartPrice}</td>
                                <td>
                                    <ul>
                                        {order.cartItemDTO.map(item => (
                                            <li key={item.dishName}>
                                                {item.dishName} - {item.quantity} x ₹{item.price} = ₹{item.totalPrice}
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

export default OrderHistory;
