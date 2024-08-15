import React, { useState, useEffect } from "react";
import axios from "axios";

const Cart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [message, setMessage] = useState("");

  useEffect(() => {
    // Fetch the cart details on component mount
    axios.get("/api/student/cart") // Update with your API endpoint
      .then(response => {
        if (response.data.cartItems) {
          setCartItems(response.data.cartItems);
          setTotalAmount(response.data.totalAmount);
        } else {
          setMessage(response.data.message);
        }
      })
      .catch(error => {
        console.error("Error fetching cart data:", error);
      });
  }, []);

  const placeOrder = () => {
    axios.post("/api/orders", cartItems) // Update with your API endpoint
      .then(response => {
        setMessage(response.data.message);
      })
      .catch(error => {
        console.error("Error placing order:", error);
      });
  };

  return (
    <div>
      <h2>Your Cart</h2>
      {message && <p>{message}</p>}
      {cartItems.length > 0 ? (
        <div>
          {cartItems.map((item, index) => (
            <div key={index}>
              <h4>{item.dishName}</h4>
              <p>Quantity: {item.quantity}</p>
              <p>Price: {item.price}</p>
              <p>Total: {item.totalPrice}</p>
            </div>
          ))}
          <h3>Total Amount: {totalAmount}</h3>
          <button onClick={placeOrder}>Place Order</button>
        </div>
      ) : (
        <p>Your cart is empty.</p>
      )}
    </div>
  );
};

export default Cart;
