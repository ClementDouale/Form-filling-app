// import React from "react";
// import { Nav, NavLink, NavMenu, UserName }from "./NavbarElements";
// import { useNavigate } from 'react-router-dom';
// import axios from "axios";
// import Cookies from 'js-cookie'

// const getUserData = (userId) => {
//     return axios.get(`http://localhost:8081/users/${userId}`);
//   };

// const Navbar = () => {

//     // Get the `navigate` function from the `useNavigate` hook
//     const navigate = useNavigate();

//     const handleLogout = () => {
//         // Remove the JWT from local storage
//         //localStorage.removeItem('jwt');
//         Cookies.remove('userId');
//         Cookies.remove('jwt');
//        // Redirect the user to the login page
//         navigate('/login');
//     };

//     const userData="Rayenn";

//     return (
//         <>
//             <Nav>
//                 <NavMenu>
//                     <NavLink to="/" activeStyle>
//                         Accueil
//                     </NavLink>
//                     <NavLink to="/forms" activeStyle>
//                         Formulaires
//                     </NavLink>
//                     <NavLink to="/journeys" activeStyle>
//                         Parcours
//                     </NavLink>
//                 </NavMenu>
//                 <UserName>Welcome {userData}</UserName>
//                 <button className="nav" onClick={handleLogout}>Disconnect</button>
//             </Nav>
//         </>
//     );
// };

// export default Navbar;


import React, { useState, useEffect } from "react";
import { Nav, NavLink, NavMenu, UserName }from "./NavbarElements";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import Cookies from 'js-cookie';
import { API_URL } from '../../constants';

const getUserData = (userId) => {
    return axios.get(`${API_URL}/user/${userId}`, {
      headers: {
        //'Content-Type': 'application/json',
        'Authorization': `Bearer ${Cookies.get('jwt')}`
      }
    });
  };

const Navbar = () => {
    // Initialize state variables
    const [userData, setUserData] = useState(null);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    // Get the `navigate` function from the `useNavigate` hook
    const navigate = useNavigate();

    const handleLogout = () => {
        // Remove the JWT and user ID from Cookies
        Cookies.remove('userId');
        Cookies.remove('jwt');
       // Redirect the user to the login page
        navigate('/login');
    };

    // Fetch the user data when the component mounts
    useEffect(() => {
        const userId = Cookies.get('userId');
        if (!userId) {
            setError("User ID not found in Cookies");
            setIsLoading(false);
            return;
        }
        getUserData(userId)
            .then((response) => {
                setUserData(response.data);
                setIsLoading(false);
            })
            .catch((error) => {
                setError(error);
                setIsLoading(false);
            });
    }, []);


    // Render a loading spinner while the data is being fetched
    if (isLoading) {
        return <div>Loading...</div>;
    }

    // Render an error message if there was a problem fetching the data
     if (error) {
         return <div>Error: {error}</div>;
     }

     let name=userData.name.replace("null", "");

    return (
        <>
            <Nav>
                <NavMenu>
                    <NavLink to="/" activeStyle>
                        Accueil
                    </NavLink>
                    {/* <NavLink to="/profile" activeStyle>
                        Profil
                    </NavLink> */}
                    <NavLink to="/forms" activeStyle>
                        Formulaires
                    </NavLink>
                    <NavLink to="/journeys" activeStyle>
                        Parcours
                    </NavLink>
                </NavMenu>
                <UserName>Bonjour {name}</UserName>
                <button className="nav" onClick={handleLogout}>Deconnexion</button>
            </Nav>
        </>
    );
};

export default Navbar;

