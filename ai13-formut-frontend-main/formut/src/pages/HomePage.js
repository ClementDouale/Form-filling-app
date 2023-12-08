import React, { useEffect, useState } from 'react';
import Navbar from '../components/navbar/Navbar';
import "../styles/Home.css";
import 'font-awesome/css/font-awesome.min.css';
import axios from 'axios';
import Cookies from 'js-cookie'
import { API_URL } from '../constants';
import { useNavigate } from 'react-router-dom';


const Home = () => {
  
  const navigate = useNavigate();
  const handleFormPageButton = () => {
    navigate('/forms');
  };

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

      //user stats
      axios.get(`${API_URL}/user/${Cookies.get('userId')}/statistics`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${Cookies.get('jwt')}`
        }
      })
     .then(response => {
       setUserStats(response.data);
       console.log(response.data)
       console.log("stats:",userStats)
     })
      .catch(error => {
        console.error(error);
      });

  }, []);

  
  // define the journeys state
  const [journeys, setJourneys] = useState([]);
  const [formNames, setFormNames] = useState({});
  const [userStats, setUserStats] = useState({});
  const firstFiveJourneys = journeys.slice(0, 5);

  return (
    <div>
    <Navbar />
    
    <div class="container">
    {!userStats || !Object.keys(userStats).length ? (
        <div class="items">
        <div class="items-head">
          <p>Statistiques</p>
          <hr></hr>
        </div>
        <div class="items-body">
      <p>Vous n'avez pas encore de statistiques</p>
      </div>
      </div>
      
    ) : (
      <div class="items">
      <div class="items-head">
        <p>Statistiques</p>
        <hr></hr>
      </div>
      <div class="items-body">
          <div class="items-body-content">
            Nombre de parcours : {userStats.journeysAmount}  
          </div>
          <div class="items-body-content">
           Score moyen : {userStats.averageScore.toFixed(2)*100}% 
          </div>
          <div class="items-body-content">
            Durée moyenne : {userStats.averageDuration}s  
          </div>
          <div class="items-body-content">
            Meilleur score : {userStats.bestScore.toFixed(2)*100}% 
          </div>
          <div class="items-body-content">
            Pire score : {userStats.worstScore.toFixed(2)*100}%  
          </div>
      </div>    
    </div> 
    )
     }
      {firstFiveJourneys.length > 0 ? (
        <div>
          <div class="items">
            <div class="items-head">
              <p>Parcours récents</p>
              <hr></hr>
            </div>
          <div class="items-body">
          {firstFiveJourneys.map(journey => (
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
              <hr></hr>
            </div>
            <div class="items-body">
        <p>Vous n'avez fait aucun parcours</p>
        </div>
        </div>
        
            </div>
          
      )}
      <button className="primary" onClick={handleFormPageButton}>Démarrer un nouveau parcours</button>
      <div class="marge"></div>
    
    </div>
    </div>
    
    

  );
};

export default Home;