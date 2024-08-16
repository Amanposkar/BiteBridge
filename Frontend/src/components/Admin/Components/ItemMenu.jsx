import React, { useState, useEffect } from "react";
import axiosInstance from "../axiosInstance"; // Adjust the path based on your directory structure
import "../styles/ItemMenu.css";

const ItemMenu = () => {
  const [items, setItems] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    price: "",
    quantity: "",
    id: "",
  });
  const [action, setAction] = useState(""); // 'add', 'update', or 'delete'
  const [selectedIndex, setSelectedIndex] = useState(null);

  // Fetch dishes from the backend when the component mounts
  useEffect(() => {
    fetchDishes();
  }, []);

  const fetchDishes = async () => {
    try {
      const response = await axiosInstance.get("/admin/dishes");
      setItems(response.data);
    } catch (error) {
      console.error("Error fetching dishes:", error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    const valueAsNumber =
      name === "price" || name === "quantity" ? parseFloat(value) : value;
    setFormData({ ...formData, [name]: valueAsNumber });
  };

  const handleActionChange = (newAction) => {
    if (action === newAction) {
      setAction("");
      setFormData({ name: "", price: "", quantity: "", id: "" });
      setSelectedIndex(null);
    } else {
      setAction(newAction);
      if (newAction === "add") {
        setFormData({ name: "", price: "", quantity: "", id: "" });
        setSelectedIndex(null);
      } else if (newAction === "update") {
        setFormData({ id: "", quantity: "" });
        setSelectedIndex(null);
      } else if (newAction === "delete") {
        setFormData({ id: "" });
        setSelectedIndex(null);
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (action === "add") {
      try {
        const newItem = {
          name: formData.name,
          price: formData.price,
          quantity_remaining: formData.quantity,
        };

        const response = await axiosInstance.post("/admin/add/dish", newItem);
        setItems((prevItems) => [...prevItems, response.data]); // Add new item to the state
      } catch (error) {
        console.error("Error adding dish:", error);
      }
    } else if (action === "update" && formData.id) {
      try {
        const itemId = parseInt(formData.id, 10);
        const existingItem = items.find((item) => item.id === itemId);

        if (existingItem) {
          const updatedQuantity = existingItem.quantity_remaining + formData.quantity;

          await axiosInstance.patch(`/admin/updatedish/${itemId}/${formData.quantity}`);
          setItems((prevItems) =>
            prevItems.map((item) =>
              item.id === itemId ? { ...item, quantity_remaining: updatedQuantity } : item
            )
          );
        }
      } catch (error) {
        console.error("Error updating dish:", error);
      }
    } else if (action === "delete") {
      try {
        const itemId = formData.id;
        if (!itemId) {
          alert("Please enter a dish ID to delete");
          return;
        }

        await axiosInstance.delete(`/admin/removedish/${itemId}`);
        setItems((prevItems) =>
          prevItems.filter((item) => item.id !== parseInt(itemId, 10))
        );
      } catch (error) {
        console.error("Error deleting dish:", error);
      }
    }

    setFormData({ name: "", price: "", quantity: "", id: "" });
    setAction("");
    setSelectedIndex(null);
  };

  return (
    <div className="item-menu-container">
      <div className="button-group">
        <button onClick={() => handleActionChange("add")}>Add Item</button>
        <button onClick={() => handleActionChange("delete")}>
          Delete Item
        </button>
        <button onClick={() => handleActionChange("update")}>
          Update Item
        </button>
      </div>

      {action && (
        <div className="form-container">
          <h2>{action.charAt(0).toUpperCase() + action.slice(1)} Item</h2>
          <form onSubmit={handleSubmit}>
            {action === "update" && (
              <>
                <label>
                  Dish ID:
                  <input
                    type="number"
                    name="id"
                    value={formData.id || ""}
                    onChange={handleInputChange}
                    required
                  />
                </label>
                <label>
                  Quantity:
                  <input
                    type="number"
                    name="quantity"
                    value={formData.quantity || ""}
                    onChange={handleInputChange}
                    required
                  />
                </label>
              </>
            )}
            {action === "add" && (
              <>
                <label>
                  Name:
                  <input
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleInputChange}
                    required
                  />
                </label>
                <label>
                  Price:
                  <input
                    type="number"
                    name="price"
                    value={formData.price || ""}
                    onChange={handleInputChange}
                    required
                    step="0.01"
                  />
                </label>
                <label>
                  Quantity:
                  <input
                    type="number"
                    name="quantity"
                    value={formData.quantity || ""}
                    onChange={handleInputChange}
                    required
                  />
                </label>
              </>
            )}
            {action === "delete" && (
              <label>
                Dish ID:
                <input
                  type="number"
                  name="id"
                  value={formData.id || ""}
                  onChange={handleInputChange}
                  required
                />
              </label>
            )}
            <button className="itemMenu" type="submit">
              {action.charAt(0).toUpperCase() + action.slice(1)}
            </button>
          </form>
        </div>
      )}

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Quantity</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr
              key={item.id} // Use a unique key for each item
              onClick={() => {
                setSelectedIndex(items.indexOf(item));
                if (action === "update") {
                  setFormData(item);
                }
              }}
              className={
                selectedIndex === items.indexOf(item) ? "selected" : ""
              }
            >
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>
              ₹{item.price !== undefined ? item.price.toFixed(2) : "N/A"}
              </td>
              <td>
                {item.quantity_remaining !== undefined
                  ? item.quantity_remaining
                  : "N/A"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ItemMenu;
