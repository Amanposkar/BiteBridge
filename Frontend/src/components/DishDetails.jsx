import React from 'react';

export default function DishDetails(props) {
  return (
    <tr>
      <td>{props.prod.pid}</td>
      <td>{props.prod.pname}</td>
      <td>{props.prod.qty}</td>
      <td>{props.prod.price}</td>
    </tr>
  );
}
