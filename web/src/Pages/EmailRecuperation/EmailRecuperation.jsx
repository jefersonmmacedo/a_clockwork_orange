import './emailRecuperation.css';
import React, { useContext, useState } from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';
import {toast} from 'react-toastify';
import { AuthContext } from '../../Contexts/Auth';



export default function EmailRecuperation() {
   const [email, setEmail] = useState('');
  const {EmailRecuperation} = useContext(AuthContext);
  

  async function handleClick(e) {
    e.preventDefault()
    
    if(email.includes('@fcamara.com.br')) {
      EmailRecuperation(email);
    } else {
      toast.error('Acesse com seu e-mail corporativo');
    }
  
   }

  

  return (
    <div className="container">
      <div className="content">
        <div className="emailRecuperation">
          <div className="infos">
            <ImageBody image={work} alt="image-computer"/>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <form action="">
                <span>E-mail</span>
                <input type="text" placeholder="Digite seu e-mail fcamara" defaultValue={email}  onChange={(e) => setEmail(e.target.value)}/>
                <button className="button-primary" onClick={handleClick}>Acessar</button>
                </form>
            </div>
          </div>
         </div>
         <Footer />
      </div>
    </div>
  );
};