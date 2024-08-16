import React, { useState, useEffect } from "react";
import axiosInstance from "../axiosInstance"; // Import the Axios instance
import "../styles/studentsAccount.css";

// AddStudentForm Component
const AddStudentForm = ({ onStudentAdded }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [prn, setPrn] = useState("");
  const [tokensBalance, setTokensBalance] = useState("");
  const [mobileNo, setMobileNo] = useState("");
  const [errors, setErrors] = useState({});

  const handleSubmit = async (e) => {
    e.preventDefault();
    let formErrors = {};

    // Validate inputs
    if (!password || password.length < 6) {
      formErrors.password = "Password must be at least 6 characters.";
    }
    if (!firstName) {
      formErrors.firstName = "First name is required.";
    }
    if (!lastName) {
      formErrors.lastName = "Last name is required.";
    }
    if (!prn || prn.length !== 12 || isNaN(prn)) {
      formErrors.prn = "PRN must be exactly 12 digits.";
    }
    if (tokensBalance === "" || tokensBalance < 0) {
      formErrors.tokensBalance = "Tokens must be a non-negative number.";
    }
    if (!mobileNo || mobileNo.length < 10) {
      formErrors.mobileNo = "Valid phone number is required.";
    }

    if (Object.keys(formErrors).length === 0) {
      try {
        // Concatenate +91 prefix to mobile number
        const fullMobileNo = `+91${mobileNo}`;

        await axiosInstance.post("/admin/add/student", {
          username,
          password,
          first_name: firstName, // Updated to match database column
          last_name: lastName,  // Updated to match database column
          prn,
          tokens_balance: tokensBalance, // Updated to match database column
          mobileNo: fullMobileNo, // Updated to match database column
        });
        alert("Student added successfully");
        setUsername("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setPrn("");
        setTokensBalance("");
        setMobileNo("");
        if (onStudentAdded) onStudentAdded(); // Notify parent component
      } catch (error) {
        console.error("Error adding student", error);
      }
    } else {
      setErrors(formErrors);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "password") setPassword(value);
    if (name === "firstName") setFirstName(value);
    if (name === "lastName") setLastName(value);
    if (name === "prn") {
      setPrn(value);
      setUsername(value); // Generate username based on PRN
    }
    if (name === "tokensBalance") setTokensBalance(value);
    if (name === "mobileNo") setMobileNo(value);
    setErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
  };

  return (
    <form onSubmit={handleSubmit} className="forms">
      <h3>Add Student</h3>
      <label>
        Username (Email):
        <input
          type="email"
          name="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Enter student email"
          readOnly
        />
        {errors.username && <p className="error">{errors.username}</p>}
      </label>
      <label>
        Password:
        <input
          type="password"
          name="password"
          value={password}
          onChange={handleChange}
          placeholder="Enter password"
          required
        />
        {errors.password && <p className="error">{errors.password}</p>}
      </label>
      <label>
        First Name:
        <input
          type="text"
          name="firstName"
          value={firstName}
          onChange={handleChange}
          placeholder="Enter first name"
          required
        />
        {errors.firstName && <p className="error">{errors.firstName}</p>}
      </label>
      <label>
        Last Name:
        <input
          type="text"
          name="lastName"
          value={lastName}
          onChange={handleChange}
          placeholder="Enter last name"
          required
        />
        {errors.lastName && <p className="error">{errors.lastName}</p>}
      </label>
      <label>
        PRN:
        <input
          type="text"
          name="prn"
          value={prn}
          onChange={handleChange}
          placeholder="Enter PRN Number"
          maxLength="12"
          pattern="\d*"
          required
        />
        {errors.prn && <p className="error">{errors.prn}</p>}
      </label>
      <label>
        Tokens Balance:
        <input
          type="number"
          name="tokensBalance"
          value={tokensBalance}
          onChange={handleChange}
          placeholder="Enter tokens balance"
          min="0"
          required
        />
        {errors.tokensBalance && (
          <p className="error">{errors.tokensBalance}</p>
        )}
      </label>
      <label>
        Phone Number (excluding +91):
        <input
          type="text"
          name="mobileNo"
          value={mobileNo}
          onChange={handleChange}
          placeholder="Enter phone number"
          maxLength="10"
          pattern="\d*"
          required
        />
        {errors.mobileNo && <p className="error">{errors.mobileNo}</p>}
      </label>
      <button type="submit">Add Student</button>
    </form>
  );
};

// DeleteStudentForm Component
const DeleteStudentForm = ({ onStudentDeleted }) => {
  const [studentId, setStudentId] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axiosInstance.delete(`/admin/student/${studentId}`);
      alert("Student deleted successfully");
      setStudentId("");
      if (onStudentDeleted) onStudentDeleted(); // Notify parent component
    } catch (err) {
      setError("Error deleting student.");
      alert("Error making this Update!");
      console.error(err);
    }
  };

  const handleChange = (e) => {
    setStudentId(e.target.value);
    setError("");
  };

  return (
    <form onSubmit={handleSubmit} className="forms">
      <h3>Delete Student</h3>
      <label>
        Student ID:
        <input
          type="text"
          value={studentId}
          onChange={handleChange}
          placeholder="Enter Student ID to delete"
          required
        />
      </label>
      {error && <p className="error">{error}</p>}
      <button type="submit">Delete Student</button>
    </form>
  );
};

// UpdateStudentForm Component
const UpdateStudentForm = ({ onStudentUpdated }) => {
  const [id, setId] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [tokensToAdd, setTokensToAdd] = useState("");
  const [prn, setPrn] = useState("");
  const [mobileNo, setMobileNo] = useState("");
  const [errors, setErrors] = useState({});

  const handleSubmit = async (e) => {
    e.preventDefault();
    let formErrors = {};

    if (!id) {
      formErrors.id = "Student ID is required.";
    }
    if (!password) {
      formErrors.password = "Password is required";
    }
    if (!firstName) {
      formErrors.firstName = "First name is required.";
    }
    if (!lastName) {
      formErrors.lastName = "Last name is required.";
    }
    if (tokensToAdd === "" || tokensToAdd < 0) {
      formErrors.tokensToAdd = "Tokens to add must be a non-negative number.";
    }
    if (!prn) {
      formErrors.prn = "PRN number is required.";
    }
    if (!mobileNo) {
      formErrors.mobileNo = "Mobile number is required.";
    }
    if (Object.keys(formErrors).length === 0) {
      try {
        // Fetch the current tokens balance
        const response = await axiosInstance.get(`/admin/student/${id}`);
        const currentTokensBalance = response.data.tokens_balance;
        // Calculate the new tokens balance
        const newTokensBalance = currentTokensBalance + parseInt(tokensToAdd);

        // Prepend +91 to mobileNo
        const formattedMobileNo = `+91${mobileNo}`;

        // API call to update the student information including the updated tokens balance
        await axiosInstance.put(`/admin/edit/student/${id}`, {
          username: prn,  // Set username as PRN
          password,
          tokens_balance: newTokensBalance,
          first_name: firstName, //need to keep these name same as the DTO 
          last_name: lastName, //if using any different names then provide the references as given here
          prn,
          mobileNo: formattedMobileNo,
        });

        alert("Student updated successfully");
        // Resetting form fields after successful update
        setId("");
        setUsername("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setTokensToAdd("");
        setPrn("");
        setMobileNo("");
        if (onStudentUpdated) onStudentUpdated(); // Notify parent component
      } catch (error) {
        console.log("error in processing the details here");
        console.error("Error updating student", error);
      }
    } else {
      setErrors(formErrors);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "id") setId(value);
    if (name === "password") setPassword(value);
    if (name === "firstName") setFirstName(value);
    if (name === "lastName") setLastName(value);
    if (name === "tokensToAdd") setTokensToAdd(value);
    if (name === "prn") {
      setPrn(value);
      setUsername(value);  // Automatically set username as PRN
    }
    if (name === "mobileNo") setMobileNo(value);
    setErrors((prevErrors) => ({ ...prevErrors, [name]: "" }));
  };

  return (
    <form onSubmit={handleSubmit} className="forms">
      <h3>Update Student</h3>
      <label>
        Student ID:
        <input
          type="text"
          name="id"
          value={id}
          onChange={handleChange}
          placeholder="Enter student ID"
          required
        />
        {errors.id && <p className="error">{errors.id}</p>}
      </label>
      <label>
        Username (Email):
        <input
          type="email"
          name="username"
          value={username}
          onChange={handleChange}
          placeholder="Username (same as PRN)"
          readOnly
          required
        />
      </label>
      <label>
        Password:
        <input
          type="password"
          name="password"
          value={password}
          onChange={handleChange}
          placeholder="Enter new password (optional)"
        />
        {errors.password && <p className="error">{errors.password}</p>}
      </label>
      <label>
        First Name:
        <input
          type="text"
          name="firstName"
          value={firstName}
          onChange={handleChange}
          placeholder="Enter first name"
          required
        />
        {errors.firstName && <p className="error">{errors.firstName}</p>}
      </label>
      <label>
        Last Name:
        <input
          type="text"
          name="lastName"
          value={lastName}
          onChange={handleChange}
          placeholder="Enter last name"
          required
        />
        {errors.lastName && <p className="error">{errors.lastName}</p>}
      </label>
      <label>
        Tokens to Add:
        <input
          type="number"
          name="tokensToAdd"
          value={tokensToAdd}
          onChange={handleChange}
          placeholder="Enter tokens to add"
          min="0"
          required
        />
        {errors.tokensToAdd && <p className="error">{errors.tokensToAdd}</p>}
      </label>
      <label>
        PRN:
        <input
          type="text"
          name="prn"
          value={prn}
          onChange={handleChange}
          placeholder="Enter PRN Number"
          maxLength="12"
          pattern="\d*"
          required
        />
        {errors.prn && <p className="error">{errors.prn}</p>}
      </label>
      <label>
        Mobile Number:
        <input
          type="text"
          name="mobileNo"
          value={mobileNo}
          onChange={handleChange}
          placeholder="Enter mobile number"
          required
        />
        {errors.mobileNo && <p className="error">{errors.mobileNo}</p>}
      </label>
      <button type="submit">Update Student</button>
    </form>
  );
};

// StudentsAccount Component
const StudentsAccount = () => {
  const [students, setStudents] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [showDeleteForm, setShowDeleteForm] = useState(false);
  const [showUpdateForm, setShowUpdateForm] = useState(false);

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    try {
      const response = await axiosInstance.get("/admin/students");
      // console.log(response.data);
      setStudents(response.data);
    } catch (error) {
      console.error("Error fetching students", error);
    }
  };

  const handleStudentAdded = () => {
    fetchStudents();
    setShowAddForm(false);
  };

  const handleStudentDeleted = () => {
    fetchStudents();
    setShowDeleteForm(false);
  };

  const handleStudentUpdated = () => {
    fetchStudents();
    setShowUpdateForm(false);
  };

  return (
    <div className="students-account">
      <h2>Student Management</h2>
      <div className="button-container">
        <button onClick={() => setShowAddForm(!showAddForm)}>
          {showAddForm ? "Cancel" : "Add Student"}
        </button>
        <button onClick={() => setShowDeleteForm(!showDeleteForm)}>
          {showDeleteForm ? "Cancel" : "Delete Student"}
        </button>
        <button onClick={() => setShowUpdateForm(!showUpdateForm)}>
          {showUpdateForm ? "Cancel" : "Update Student"}
        </button>
      </div>
      {showAddForm && <AddStudentForm onStudentAdded={handleStudentAdded} />}
      {showDeleteForm && (
        <DeleteStudentForm onStudentDeleted={handleStudentDeleted} />
      )}
      {showUpdateForm && (
        <UpdateStudentForm onStudentUpdated={handleStudentUpdated} />
      )}
      <h3>Student List</h3>
      <table className="StudentTable">
      <thead>
            <tr>
              <th>ID</th>
              <th>First Name</th>
              <th>Last Name</th>
              <th>PRN</th>
              <th>Phone Number</th>
              <th>Tokens Balance</th>
              {/* <th>Actions</th> */}
            </tr>
          </thead>
        <tbody>
          {students.map((student) => (
            <tr key={student.id}>
              <td>{student.id}</td>
              <td>{student.first_name}</td>
              <td>{student.last_name}</td>
              <td>{student.prn}</td>
              <td>{student.mobileNo}</td>
              <td>{student.tokens_balance}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default StudentsAccount;
