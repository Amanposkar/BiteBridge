import React from "react";
import Navbar from "./CanteenNavbar";
import '../styles/canteenHome.css';

export default function CanteenHome() {
  return (
    <div className="CanteenHomeComponentContainer">
      <Navbar></Navbar>
      <div className="CanteenHomeComponentFirstLayer">
      </div>
    </div>
  );
}