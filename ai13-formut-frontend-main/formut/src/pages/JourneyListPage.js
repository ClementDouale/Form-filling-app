import React, { useEffect, useState } from 'react';
import Navbar from '../components/navbar/Navbar';
import "../styles/Form.css";
import 'font-awesome/css/font-awesome.min.css';
import axios from 'axios';
import Cookies from 'js-cookie'
import { API_URL } from '../constants';
import { useNavigate } from 'react-router-dom';

const JourneyList = () => {
  
    const navigate = useNavigate();
  
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
  
    const handleJourneyButton = (id) => {
      console.log(id);
      navigate(`/journey/${id}`, { state: { journeyId: id } });
    };
  
    useEffect(() => {
      const isLoggedIn = checkIfLoggedIn();   //A REMETTRE
      //const isLoggedIn = true;  //A ENLEVER
      if (!isLoggedIn) {
        // the user is not logged in, redirect to login page
        window.location.replace('/login');
      }
  
      const userId = Cookies.get('userId');
      axios.get(`${API_URL}/user/${userId}/journey`, {
        headers: {
          //'Content-Type': `application/json`,
          'Authorization': `Bearer ${Cookies.get('jwt')}`
        }
      })
        .then(response => {
          // set the journeys state with the response data
          setJourneys(response.data);
          console.log(response.data);
          const formNames = {};
          response.data.forEach(journey => {
            if (!formNames[journey.formId]) {
              axios.get(`${API_URL}/form/${journey.formId}`, {
                headers: {
                  'Authorization': `Bearer ${Cookies.get('jwt')}`
                }
              })
             .then(response => {
               formNames[journey.formId] = response.data.subject;
               setFormNames(prevFormNames => ({...prevFormNames, ...formNames}));
               console.log(formNames)
             })
              .catch(error => {
                console.error(error);
              });
        }})})
        .catch(error => {
          console.error(error);
        });
  
  
    }, []);
  
    
    // define the journeys state
    const [journeys, setJourneys] = useState([]);
    const [formNames, setFormNames] = useState({});

  
    return (
      <div>
      <Navbar />
      <div class="container">
        
        {journeys.length > 0 ? (
          <div>
            <div class="items">
              <div class="items-head">
                <p>Parcours réalisés</p>
                <hr></hr>
              </div>
            <div class="items-body">
            {journeys.map(journey => (
              <div class="items-body-content">
              <span key={journey.id}>
              {formNames[journey.formId]} | 
              Score: {journey.score.toFixed(2)*100}% | 
              Durée: {journey.duration}s
              </span>
              <button class="list" onClick={() => handleJourneyButton(journey.id)}>
                        <i  class="fa fa-angle-right"></i>
                      </button>
              </div>
            ))}
          </div>
              </div>
            </div>
        ) : (
          <div>
            <div class="items">
              <div class="items-head">
                <p>Parcours récents</p>
              </div>
              <div class="items-body">
          <p>Vous n'avez fait aucun parcours</p>
          </div>
          </div>
          
              </div>
            
        )}
      </div>
      </div>
      
      
  
    );
  };
  
  export default JourneyList;