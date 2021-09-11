import './login.css';
import React from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';



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
            <ImageBody image={work} alt="image-computer"/>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <span>E-mail</span>
                <input type="text" placeholder="Digite seu e-mail fcamara"/>
                <button className="button-primary" onClick={handleRedirect}>Acessar</button>
            </div>
          </div>
         </div>
         <Footer />
      </div>
    </div>
  );
};