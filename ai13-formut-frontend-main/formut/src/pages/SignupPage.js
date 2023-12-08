import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import "../styles/Login.css";

function SignupPage() {
  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [society, setSociety] = useState('');
  const [phone, setPhone] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLoginButton = () => {
    navigate('/login');
  };


  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      
      const response = await axios.post('http://localhost:8080/user/register', { email, name, surname, society, phone });
      alert("Password envoyé par mail")
      navigate('/login');
    } catch (err) {
      if (err.response.status === 409) {
        setError("Adresse mail déjà utilisée, veuillez vous connecter");
        //alert("Vous êtes déjà inscrit");
      } else {
        alert("Erreur lors de l'inscription, veuillez réessayer");
      }
    }
  };

  return (
    <div className="App">
    <form className="form" onSubmit={handleSubmit}>
    <div className="input-group">
      <label htmlFor="email">Email :</label>
      <input type="email" id="email" value={email} onChange={(event) => setEmail(event.target.value)} />
      <br />
    </div>
    <div className="input-group">
      <label htmlFor="name">Name :</label>
      <input type="text" id="name" value={name} onChange={(event) => setName(event.target.value)} />
      <br />
    </div>
    <div className="input-group">
      <label htmlFor="surname">Surname :</label>
      <input type="text" id="surnname" value={surname} onChange={(event) => setSurname(event.target.value)} />
      <br />
    </div>
    <div className="input-group">
      <label htmlFor="society">Society :</label>
      <input type="text" id="society" value={society} onChange={(event) => setSociety(event.target.value)} />
      <br />
    </div>
    <div className="input-group">
      <label htmlFor="phone">Phone number :</label>
      <input type="text" id="phone" value={phone} onChange={(event) => setPhone(event.target.value)} />
      <br />
    </div>
    <br />
      {error && <span style={{ color: 'red' }}>{error}</span>}
      <br />
      <button className="primary" type="submit">Sign Up</button>
      <button className="secondary" onClick={handleLoginButton}>Log In</button>
    </form>
    </div>
  );
}

export default SignupPage;



