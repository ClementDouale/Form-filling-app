import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route}
  from 'react-router-dom';

import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import QuizzListPage from './pages/FormListPage';
import JourneyListPage from './pages/JourneyListPage';
import ProfilePage from './pages/ProfilePage';
import JourneyPage from './pages/JourneyPage';
import FormPage from './pages/FormPage';
import SignupPage from './pages/SignupPage';

function App() {
  return (
    <Router>
    <Routes>
      <Route exact path='/' element={<HomePage />} />
      <Route path="/login" element={<LoginPage/>} />
      <Route path="/signup" element={<SignupPage/>} />
      <Route path="/forms" element={<QuizzListPage/>}/>
      <Route path="/journeys" element={<JourneyListPage/>}/>
      <Route path="/profile" element={<ProfilePage/>}/>
      <Route path="/journey/:journeyId" element={<JourneyPage/>}/>
      <Route path="/form/:formId" element={<FormPage/>}/>
    </Routes>
    </Router>
  );
  }

export default App;