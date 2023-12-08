import React, { useEffect, useState } from 'react';
import Navbar from '../components/navbar/Navbar';
import 'font-awesome/css/font-awesome.min.css';
import "../styles/Journey.css";
import axios from 'axios';
import Cookies from 'js-cookie'
import { API_URL } from '../constants';
import { useNavigate, useParams } from 'react-router-dom';

const Page = () => {

  // retrieve the url param 
  const { journeyId } = useParams()
  //console.log(formId);

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
  const handleHomePageButton = () => {
    navigate('/');
  };

  useEffect(() => {
    const isLoggedIn = checkIfLoggedIn();
    if (!isLoggedIn) {
      // the user is not logged in, redirect to login page
      window.location.replace('/login');
    }

    axios.get(`${API_URL}/journey/${journeyId}`, {
      headers: {
        'Authorization': `Bearer ${Cookies.get('jwt')}`
      }
    })
      .then(response => {
        setJourney(response.data);
      })
      .catch(error => {
        if (error.response.status === 404) {
          alert("Le parcours n'existe pas");
        } else {
          alert("Erreur inconnue");
        }
      });

  }, [journeyId]);

  const [journey, setJourney] = useState({});

    
return (
  <div>
    <Navbar/>
    <div class="container">
    {!journey || !Object.keys(journey).length ? (
      <p>No journey to display</p>
    ) : (
      <div >
        <div class="items">
        <p>Score : {journey.score.toFixed(2)*100}%</p> <br/>
        <p>Durée : {journey.duration}s</p>
          </div>
          {journey.questions.map((question, index) => (
          <div key={index}>
            <div class="items">
            <div class="items-head">
           <p>{question.question.wording}</p>
           <hr></hr>
           </div>
           <div class="items-body">
           <ul>
             {question.answers.map((answer, index) => (
              <div class="items-body-content">
                <li 
                  key={index}
                  className={`answer ${answer.id === question.answer_good ? 'good-answer' : ''} ${answer.id === question.answer_user ? 'user-answer' : ''} ${answer.id === question.answer_good && answer.id === question.answer_user ? 'good-user-answer' : ''}`}
                  >
                 {answer.content}
               </li>
               </div>
              ))}
            </ul>
            </div>
            </div>
          </div>
        ))}
      </div>
    )}
    
    <button className="primary" onClick={handleHomePageButton}>Retour à l'accueil</button>
    <div class="marge"></div>
    </div>
  </div>
);





};
  
export default Page;