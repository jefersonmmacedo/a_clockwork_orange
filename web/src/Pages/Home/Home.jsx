import './home.css';
import React from 'react';
import { useHistory } from "react-router-dom";



export default function Home() {

  const history = useHistory();

  const routeChange = () =>{ 
    let path = './Login/Login.jsx'; 
    history.push(path);
  }
  

  return (
    <div className="Home">
      <div className="Logo"></div>
      <div className="Email">E-mail</div>
      <input type="text" className="InputEmail" />
      <button className="Button" onClick={routeChange}>ACESSAR</button>
      <div className="Logo2"></div>
    </div>
  );
};