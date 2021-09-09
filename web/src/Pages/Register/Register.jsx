import './register.css';
import React from 'react';
import logoAside2 from '../../assets/images/logoAside2.svg';
import smartphoneAside from '../../assets/images/smartphoneAside.svg';
import logoFcamaraSquad34 from '../../assets/images/logoFcamaraSquad34.svg';
import {useHistory} from 'react-router-dom'



export default function Register() {
  const history = useHistory();

  function handleRedirect() {
   history.push("/")
  }

  return (
    <div className="container">
      <div className="content">
        <div className="register">
          <div className="infos">
            <div className="image">
                <img src={smartphoneAside} alt="image-computer" />
            </div>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />

                <span>Nome Completo</span>
                <input type="text" placeholder="Digite seu nome completo"/>
                 <span>Qual é a sua função?</span>
                <input type="text" placeholder="Digite seu código de acesso"/>
                <span>Senha</span>
                <input type="text" placeholder="Senha"/>
                <span>Confirmar senha</span>
                <input type="text" placeholder="Confirmar senha"/>
                <button className="button-primary" onClick={handleRedirect}>Criar conta</button>
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