import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import "../styles/Login.css";
import Cookies from 'js-cookie'



const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const handleSignupButton = () => {
    navigate('/signup');
    
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      // Send a request to the server to authenticate the user
      const response = await axios.post('http://localhost:8080/user/login', {
        username,
        password,
      });
  
      // If the server returns a JWT, store it in the browser and set isLoggedIn to true
      //const { token } = response.data;
      //token = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImF1dGhvcml6YXRpb24iOiJhZG1pbiJ9.f7iKN-xi24qrQ5NQtOe0jiriotT-rve3ru6sskbQXnA
      //localStorage.setItem('jwt', token);
      //const payload = jwt.decode(jwt);
      //print(payload)
      //localStorage.setItem('jwtPayload', JSON.stringify(payload));
      const jwt = response.data.token;
      const userId = response.data.userID;
      Cookies.set('userId', userId);
      Cookies.set('jwt', jwt);
      setIsLoggedIn(true);
    } catch (err) {
      if (err.response.status === 409) {
        setError('Email or password invalide');
      } else {
        alert("Erreur lors du login");
      }
    }
  };

  if (isLoggedIn) {
    navigate('/');
  }

  return (
    <div className="App">
    <form className="form" onSubmit={handleSubmit}>
    <div className="input-group">
      <label htmlFor="username">Email:</label>
      <input
        type="text"
        id="username"
        value={username}
        onChange={(event) => setUsername(event.target.value)}
      />
      </div>
      <br />
      <div className="input-group">
      <label htmlFor="password">Password:</label>
      <input
        type="password"
        id="password"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
      />
      </div>
      <br />
      {error && <span style={{ color: 'red' }}>{error}</span>}
      <br />
      <button className="primary" type="submit">Log In</button>
      <button className="secondary" onClick={handleSignupButton}>Sign Up</button>
    </form>
    </div>
  );
};

export default LoginPage;

