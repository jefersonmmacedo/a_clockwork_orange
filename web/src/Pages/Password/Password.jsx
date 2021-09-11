import './password.css';
import React from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';



export default function Password() {
  const history = useHistory();

  function handleRedirect() {
   history.push("/codesecurity")
  }

  return (
    <div className="container">
      <div className="content">
        <div className="password">
          <div className="infos">
          <ImageBody image={work} alt="image-computer"/>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <span>Senha</span>
                <input type="text" placeholder="Digite sua senha"/>
                <button className="button-primary" onClick={handleRedirect}>Entrar</button>
                <span>Esqueci a Senha</span>
            </div>
          </div>
           </div>
           <Footer />
      </div>
    </div>
  );
};