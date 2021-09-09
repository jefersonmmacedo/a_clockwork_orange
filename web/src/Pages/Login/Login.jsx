import './login.css';
import React from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import logoFcamaraSquad34 from '../../assets/images/logoFcamaraSquad34.svg';
import {useHistory} from 'react-router-dom'



export default function Login() {
  const history = useHistory();

  function handleRedirect() {
   history.push("/password")
  }

  return (
    <div className="container">
      <div className="content">
        <div className="login">
          <div className="infos">
            <div className="image">
                <img src={work} alt="image-computer" />
            </div>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <span>E-mail</span>
                <input type="text" placeholder="Digite seu e-mail fcamara"/>
                <button className="button-primary" onClick={handleRedirect}>Acessar</button>
            </div>
          </div>
          <div className="footer">
               <img src={logoFcamaraSquad34} alt="image-computer" />
          </div>
        </div>
      </div>
    </div>
  );
};