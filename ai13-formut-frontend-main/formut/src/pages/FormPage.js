import React, { useEffect, useState } from 'react';
import Navbar from '../components/navbar/Navbar';
import 'font-awesome/css/font-awesome.min.css';
import "../styles/Form.css";
import axios from 'axios';
import Cookies from 'js-cookie'
import { API_URL } from '../constants';
import { useNavigate, useParams } from 'react-router-dom';

const Form = () => {

  // retrieve the url param 
  const { formId } = useParams()
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

  // define the form state and the answers state
  const [form, setForm] = useState([]);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [startTime,setStartTime] = useState(0);
  
  
  const navigate = useNavigate();

  useEffect(() => {
    
    setStartTime(Date.now());

    const isLoggedIn = checkIfLoggedIn();   
    if (!isLoggedIn) {
      // the user is not logged in, redirect to login page
      window.location.replace('/login');
    }

    const userId = Cookies.get('userId');
    axios.get(`${API_URL}/form/${formId}/content`, {
      headers: {
        'Authorization': `Bearer ${Cookies.get('jwt')}`
      }
    })
      .then(response => {
        setForm(response.data);
      })
      .catch(error => {
        if (error.response.status === 404) {
          alert("Le formulaire n'existe plus ou il est vide");
        } else {
          alert("Erreur inconnue");
        }
      });
    
    //Set to default in the load of the page
    setCurrentQuestionIndex(0);
    setSelectedAnswers({});

    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
        window.removeEventListener("beforeunload", handleBeforeUnload);
    }
  }, []);
    
  const handleBeforeUnload = (event) => {
    setCurrentQuestionIndex(0);
    setSelectedAnswers({});
};

  const handleAnswerChange = (event, questionId) => {
    setSelectedAnswers({
      ...selectedAnswers,
      [questionId]: {
        questionId: questionId,
        answerId: event.target.value
      }
    });
  };

  const handleNextQuestion = () => {
    if (currentQuestionIndex < form.length - 1 && selectedAnswers[form[currentQuestionIndex].question.id]) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else if(!selectedAnswers[form[currentQuestionIndex].question.id]){
      alert('Please answer the current question before moving to the next one');
    }
  };


  const handleSubmit = (event) => {
    event.preventDefault();
    //if (Object.keys(selectedAnswers).length === form.length) {
    if (currentQuestionIndex == form.length - 1 && selectedAnswers[form[currentQuestionIndex].question.id]) {
      const endTime = Date.now();
      const duration = endTime - startTime;
      const durationInSeconds = duration/1000;
      const data = {
        duration: durationInSeconds,
        answers: Object.values(selectedAnswers).map(answer => ({
          answerId: answer.answerId,
          questionId: answer.questionId
        }))
      }
      console.log("Donnees envoyees au backend pour creer formulaire:",JSON.stringify(data));
      axios.post(`${API_URL}/user/${Cookies.get('userId')}/form/${formId}`, JSON.stringify(data),{headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Cookies.get('jwt')}`
      }})
         .then(response => {
          const id = response.data.id
          navigate(`/journey/${id}`, { state: { journeyId: id } });
         })
         .catch(error => {
           console.error(error);
         });
      setCurrentQuestionIndex(0);
      setSelectedAnswers({});
    } else {
      alert('Please answer all questions before submitting');
    }
  };
  
  

  
  return (
      <div>
        <Navbar/>
      {form.length > 0 ? (
        <div class="container">
      <form>
      <div class="items">
      <div class="items-head">
        <p>{form[currentQuestionIndex].question.wording}</p>
        <hr></hr>
        </div>
        <div class="items-body">
          {form[currentQuestionIndex].answers.map(answer => (
            <div key={answer.id}>
              <div class="items-body-content">
              <label>{answer.content}</label>
              <input
                type="radio"
                name={form[currentQuestionIndex].question.id}
                value={answer.id}
                onChange={e => handleAnswerChange(e, form[currentQuestionIndex].question.id)}
              />
              </div>
            </div>
          ))}
        </div>
        {currentQuestionIndex !== form.length - 1 && 
          <button type="button" className="secondary" onClick={handleNextQuestion}>Next</button>
        }
        {currentQuestionIndex === form.length - 1 && 
          <button type="submit" className="primary" onClick={handleSubmit} >
            Submit
          </button>
        }
        </div>
      </form>
      </div>
      ) : (<p>No formular to display</p>)}
      </div>
    );
    
};
  
export default Form;