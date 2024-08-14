const express = require('express');
const cors = require('cors');
const app = express();
const port = 3001;

app.use(cors());
app.use(express.json());

let products = [
  { pid: 12, pname: "chair", qty: 34, price: 3450 },
  { pid: 13, pname: "table", qty: 50, price: 5000 },
  { pid: 14, pname: "shelf", qty: 60, price: 2000 },
  { pid: 15, pname: "sofa", qty: 10, price: 50000 }
];

app.get('/products', (req, res) => res.json(products));
app.get('/products/product/:pid', (req, res) => {
  const product = products.find(p => p.pid == req.params.pid);
  if (product) res.json(product);
  else res.status(404).send('Product not found');
});
app.post('/products', (req, res) => {
  const newProduct = req.body;
  products.push(newProduct);
  res.status(201).json(newProduct);
});
app.put('/products/:pid', (req, res) => {
  const index = products.findIndex(p => p.pid == req.params.pid);
  if (index !== -1) {
    products[index] = { ...products[index], ...req.body };
    res.json(products[index]);
  } else res.status(404).send('Product not found');
});
app.delete('/products/:pid', (req, res) => {
  const index = products.findIndex(p => p.pid == req.params.pid);
  if (index !== -1) {
    products.splice(index, 1);
    res.sendStatus(204);
  } else res.status(404).send('Product not found');
});

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
