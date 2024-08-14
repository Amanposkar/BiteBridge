import React, { useState, useEffect } from 'react';
import ProductService from '../service/productService';
import DishDetails from './DishDetails';
import "./styles/list.css";

export default function DishList() {
  const [prodarr, setProdarr] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await ProductService.getAllProducts();
        setProdarr(data);
      } catch (err) {
        setError(err.message);
        console.error("Error fetching products:", err);
      }
    };

    fetchProducts();
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <div className="listContainer">
        <table>
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Product Name</th>
              <th>Quantity</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            {prodarr.map(ob => (
              <DishDetails key={ob.pid} prod={ob} />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
