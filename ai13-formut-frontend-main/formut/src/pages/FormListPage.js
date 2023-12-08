import React, { useEffect, useState } from 'react';
import Navbar from '../components/navbar/Navbar';
import 'font-awesome/css/font-awesome.min.css';
import "../styles/FormList.css";
import axios from 'axios';
import Cookies from 'js-cookie'
import { API_URL } from '../constants';
import { useNavigate } from 'react-router-dom';

const FormList = () => {
  const checkIfLoggedIn = () => {
    const token = Cookies.get('jwt');
    if (token) {
      // the user is logged in
      return true;
    } else {
      // the user is not logged in
      return false;
    }
  };
  
  const navigate = useNavigate();
  const handleFormStartButton = (id) => {
    console.log(id);
    navigate(`/form/${id}`, { state: { formId: id } });
  };


  const handleHomePageButton = () => {
    navigate('/');
  };

  const user_id = Cookies.get('userId')


  useEffect(() => {
    const isLoggedIn = checkIfLoggedIn();   //A REMETTRE
    //const isLoggedIn = true;  //A ENLEVER
    if (!isLoggedIn) {
      // the user is not logged in, redirect to login page
      window.location.replace('/login');
    }

    // fetch the completed forms
    axios.get(`${API_URL}/user/${user_id}/journey`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Cookies.get('jwt')}`
        }
      })
      .then(response => {
        
        // Filter the forms that have already been completed by the user
        const completedForms = response.data;
        console.log("journeyList :", JSON.stringify(completedForms));
        // fetch all forms
        axios.get(`${API_URL}/form/`, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${Cookies.get('jwt')}`
            }
          })
          .then(response => {
            console.log("formList :", JSON.stringify(response.data));
            // set the journeys state with the response data
            const filteredForms = response.data.filter(form => !completedForms.some(completedForm => completedForm.formId === form.id));
            console.log("filteredFormList :", JSON.stringify(filteredForms));
            setForms(filteredForms);
            console.log("toto",forms)
          })
          .catch(error => {
            if (error.response.status === 404) {
              alert("Aucun formulaire disponible");
            } else {
              alert("Erreur inconnue");
            }
          });
      })
      .catch(error => {
        if (error.response.status === 404) {  //si le user n'a jamais rempli de formulaire
          axios.get(`${API_URL}/form/`, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${Cookies.get('jwt')}`
            }
          })
          .then(response => {
            setForms(response.data);
          })
          .catch(error => {
            if (error.response.status === 404) {
              alert("Aucun formulaire disponible");
            } else {
              alert("Erreur inconnue");
            }
          });
        } else {
          alert("Erreur inconnue");
        }
      });
  }, []);
  
  // define the journeys state
  const [forms, setForms] = useState([]);
  const filteredForms = [];

  return (
    <div class="container">
      <Navbar />
      {forms.length > 0 ? (
          <div>
            <div class="items">
              <div class="items-head">
                <p>Sujets</p>
                <hr></hr>
              </div>
              <div class="items-body">
                {forms.map(form => (
                  <div class="items-body-content">
                    <span key={form.id}>
                      {form.subject}
                    </span>
                    <button class="list" onClick={() => handleFormStartButton(form.id)}>
                      <i  class="fa fa-angle-right"></i>
                    </button>
                  </div>
                ))}
              </div>
            </div>
          </div>
      ) : (
        <p>Aucun formulaire disponible</p>
      )}
      <button className="primary" onClick={handleHomePageButton}>Retour à l'accueil</button>
    <div class="marge"></div>
    </div>
    
  );
};

export default FormList;