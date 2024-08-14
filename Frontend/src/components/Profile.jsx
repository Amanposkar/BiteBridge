import React from "react";
import "./styles/profile.css";

export default function Profile() {
  return (
    <div className="profileContainer">
      <div className="profilePic">
        <img src={`${process.env.PUBLIC_URL}/images/Saurav.jpeg`}></img>
      </div>
      <div className="profileInfo">
        <table>
          <tbody>
            <tr>
              <td>Name</td>
              <td>:</td>
              <td className="tdContent"> SAURAV GARJE</td>
            </tr>
            <tr>
              <td>Course</td>
              <td>:</td>
              <td className="tdContent"> PG-DAC</td>
            </tr>
            <tr>
              <td>Designation</td>
              <td>:</td>
              <td className="tdContent"> STUDENT</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}
