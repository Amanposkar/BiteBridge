import React, { useState, useEffect } from 'react';
import '../styles/HomeMenu.css';

function HomeMenu({ onAddToCart, onRemoveFromCart }) {
    const [foodItems, setFoodItems] = useState([]);

    useEffect(() => {
        const mockData = [
            { dish_id: 1, name: 'Pohe', price: 20, quantity: 0 },
            { dish_id: 2, name: 'Upma', price: 20, quantity: 0 },
            { dish_id: 3, name: 'Misal', price: 30, quantity: 0 },
            { dish_id: 4, name: 'Tea', price: 10, quantity: 0 },
            { dish_id: 5, name: 'Coffee', price: 15, quantity: 0 },
            { dish_id: 6, name: 'Idli', price: 20, quantity: 0 },
        ];
        setFoodItems(mockData);
    }, []);

    const handleAddClick = (id) => {
        setFoodItems(prevItems =>
            prevItems.map(item =>
                item.dish_id === id
                    ? { ...item, quantity: item.quantity + 1 }
                    : item
            )
        );

        const selectedFoodItem = foodItems.find(item => item.dish_id === id);
        onAddToCart(selectedFoodItem);
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
        onRemoveFromCart(selectedFoodItem);
    };

    return (
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
    );
}

export default HomeMenu;
