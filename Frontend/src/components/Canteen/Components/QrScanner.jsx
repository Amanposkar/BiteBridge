import React, { useEffect, useState } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";
import { useNavigate } from "react-router-dom";

const QrScanner = () => {
  const [orderId, setOrderId] = useState(null);
  const navigate = useNavigate();
  
  useEffect(() => {
    const scanner = new Html5QrcodeScanner("reader", {
      qrbox: { width: 250, height: 250 },
      fps: 5,
    });

    const onScanSuccess = (result) => {
      scanner.clear(); // Clear the scanner to avoid multiple scans
      const extractedOrderId = extractOrderIdFromString(result);
      if (extractedOrderId) {
        setOrderId(extractedOrderId);
      }
    };

    const onScanError = (err) => {
      console.warn("QR scan error:", err);
    };

    scanner.render(onScanSuccess, onScanError);

    return () => {
      scanner.clear(); // Cleanup scanner on component unmount
    };
  }, []);

  const extractOrderIdFromString = (data) => {
    const match = data.match(/ID:\s*(\d+)/);
    return match ? match[1] : null;
  };

  const handleRedirect = () => {
    if (orderId) {
      navigate(`/order/${orderId}`);
    }
  };

  return (
    <div>
      <h5>Scan your QR Code here</h5>
      <div id="reader"></div>
      {orderId && (
        <div>
          <div>Order ID: {orderId}</div>
          <button onClick={handleRedirect}>View Order Details</button>
        </div>
      )}
    </div>
  );
};

export default QrScanner;
