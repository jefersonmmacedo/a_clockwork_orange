import './codeSecurity.css';
import React from 'react';
import logoAside2 from '../../assets/images/logoAside2.svg';
import smartphoneAside from '../../assets/images/smartphoneAside.svg';
import logoFcamaraSquad34 from '../../assets/images/logoFcamaraSquad34.svg';
import {useHistory} from 'react-router-dom'



export default function CodeSecurity() {
  const history = useHistory();

  function handleRedirect() {
   history.push("/register")
  }

  return (
    <div className="container">
      <div className="content">
        <div className="code-security">
          <div className="infos">
            <div className="image">
                <img src={smartphoneAside} alt="image-computer" />
            </div>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <div className="text">
                  <p>Esse é o seu primeiro acesso ao sistema,
                    por favor informe seu código de acesso.
                  </p>
                </div>
                <span>Código de acesso</span>
                <input type="text" placeholder="Digiteseu código de acesso"/>
                <button className="button-primary" onClick={handleRedirect}>Validar</button>
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