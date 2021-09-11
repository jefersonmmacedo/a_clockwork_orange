import './home.css';
import React from 'react';
import work from '../../assets/images/work.svg';
import logoAside from '../../assets/images/logoAside.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';



export default function Home() {

  const history = useHistory();
  
  function handleRedirect() {
   history.push("/login")
  }

  return (
    <div className="container">
      <div className="content">
        <div className="home">
          <div className="infos">
           <ImageBody image={work} alt="Image Work"/>
            <div className="itens">
                <img src={logoAside} alt="Logo" />
                <h4>SISTEMA DE AGENDAMENTO</h4>
                <button className="button-primary" onClick={handleRedirect}>Acessar</button>
            </div>
          </div>
         </div>
         <Footer />
      </div>
    </div>
  );
};