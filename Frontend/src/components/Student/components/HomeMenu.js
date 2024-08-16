import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance';
import '../styles/HomeMenu.css';

function HomeMenu({ onViewCart }) {
    const [foodItems, setFoodItems] = useState([]);
    const [cartItems, setCartItems] = useState(() => {
        // Load cart items from localStorage if available
        const savedCartItems = localStorage.getItem('cartItems');
        return savedCartItems ? JSON.parse(savedCartItems) : [];
    });

    useEffect(() => {
        const fetchDishes = async () => {
            try {
                const response = await axiosInstance.get('/student/dishes');
                const dishes = response.data.map(dish => ({
                    dish_id: dish.id,
                    name: dish.name,
                    price: dish.price,
                    quantity: 0,
                }));

                // Update the quantities of the dishes based on the cart items
                const updatedDishes = dishes.map(dish => {
                    const cartItem = cartItems.find(item => item.dish_id === dish.dish_id);
                    return cartItem ? { ...dish, quantity: cartItem.quantity } : dish;
                });

                setFoodItems(updatedDishes);
            } catch (error) {
                console.error("Error fetching dishes:", error);
            }
        };

        fetchDishes();
    }, [cartItems]);

    const handleAddClick = (id) => {
        setFoodItems(prevItems =>
            prevItems.map(item =>
                item.dish_id === id
                    ? { ...item, quantity: item.quantity + 1 }
                    : item
            )
        );

        const selectedFoodItem = foodItems.find(item => item.dish_id === id);
        if (selectedFoodItem.quantity === 0) {
            const newCartItems = [...cartItems, { ...selectedFoodItem, quantity: 1 }];
            setCartItems(newCartItems);
            localStorage.setItem('cartItems', JSON.stringify(newCartItems));
        } else {
            const updatedCartItems = cartItems.map(item =>
                item.dish_id === selectedFoodItem.dish_id
                    ? { ...item, quantity: item.quantity + 1 }
                    : item
            );
            setCartItems(updatedCartItems);
            localStorage.setItem('cartItems', JSON.stringify(updatedCartItems));
        }
    };

    const handleRemoveClick = (id) => {
        setFoodItems(prevItems =>
            prevItems.map(item =>
                item.dish_id === id && item.quantity > 0
                    ? { ...item, quantity: item.quantity - 1 }
                    : item
            )
        );

        const selectedFoodItem = foodItems.find(item => item.dish_id === id);
        if (selectedFoodItem.quantity > 0) {
            const updatedCartItems = cartItems
                .map(item =>
                    item.dish_id === selectedFoodItem.dish_id && item.quantity > 0
                        ? { ...item, quantity: item.quantity - 1 }
                        : item
                )
                .filter(item => item.quantity > 0);

            setCartItems(updatedCartItems);
            localStorage.setItem('cartItems', JSON.stringify(updatedCartItems));
        }
    };

    const getTotalQuantity = () => {
        return cartItems.reduce((total, item) => total + item.quantity, 0);
    };

    const getTotalPrice = () => {
        return cartItems.reduce((total, item) => total + item.price * item.quantity, 0).toFixed(2);
    };

    return (
        <div className="home-menu-container">
            <div className="total-summary-card">
                <h3>Total Summary</h3>
                <p>Total Items: {getTotalQuantity()}</p>
                <p>Total Price: ₹{getTotalPrice()}</p>
            </div>
            <div className="food-card-container">
                {foodItems.map(item => (
                    <div key={item.dish_id} className="food-card">
                        <div className="food-details">
                            <div className="food-name">{item.name}</div>
                            <div className="food-price">₹{item.price.toFixed(2)}</div>
                        </div>
                        <div className="food-actions">
                            {item.quantity === 0 ? (
                                <button className="add-button" onClick={() => handleAddClick(item.dish_id)}>ADD</button>
                            ) : (
                                <div className="quantity-control">
                                    <button className="decrement" onClick={() => handleRemoveClick(item.dish_id)}>-</button>
                                    <span className="quantity">{item.quantity}</span>
                                    <button className="increment" onClick={() => handleAddClick(item.dish_id)}>+</button>
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
            <button className="view-cart-button" onClick={onViewCart}>
                View Cart
            </button>
        </div>
    );
}

export default HomeMenu;
