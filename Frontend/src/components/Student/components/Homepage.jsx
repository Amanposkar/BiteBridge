import React from "react";
import Navbar from "./NavbarStudent";
import '../styles/studentHome.css';

export default function Homepage() {
  return (
    <div className="StudentHomeComponentContainer">
      <Navbar></Navbar>
      <div className="StudentHomeComponentFirstLayer">
      </div>
    </div>
  );
}