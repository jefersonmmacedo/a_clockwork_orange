import './codeSecurity.css';
import React from 'react';
import logoAside2 from '../../assets/images/logoAside2.svg';
import smartphoneAside from '../../assets/images/smartphoneAside.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';



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
          <ImageBody image={smartphoneAside} alt='Image-SmartphoneUser'/>
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
          </div>
          <Footer />
      </div>
    </div>
  );
};