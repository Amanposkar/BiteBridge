// ProductService.js
import axios from 'axios';

const baseUrl = "http://localhost:3001/products";

class productService {
  async getAllProducts() {
    try {
      const response = await axios.get(baseUrl);
      return response.data;
    } catch (error) {
      console.error("Error fetching products:", error);
      throw error;
    }
  }

  async getById(pid) {
    try {
      const response = await axios.get(`${baseUrl}/product/${pid}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching product by ID:", error);
      throw error;
    }
  }

  async addProduct(product) {
    try {
      const response = await axios.post(baseUrl, product);
      return response.data;
    } catch (error) {
      console.error("Error adding product:", error);
      throw error;
    }
  }

  async updateProduct(product) {
    try {
      const response = await axios.put(`${baseUrl}/${product.pid}`, product);
      return response.data;
    } catch (error) {
      console.error("Error updating product:", error);
      throw error;
    }
  }

  async deleteProduct(pid) {
    try {
      const response = await axios.delete(`${baseUrl}/${pid}`);
      return response.data;
    } catch (error) {
      console.error("Error deleting product:", error);
      throw error;
    }
  }
}

export default new productService();
