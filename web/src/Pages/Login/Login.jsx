import './login.css';
import React, { useState } from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';
import api from '../../services/api';
import {toast} from 'react-toastify';



export default function Login() {

  const [email, setEmail] = useState('')
  const history = useHistory();

  async function handleClick(e) {
    e.preventDefault()
    
    if(email.includes('@fcamara.com.br')) {
      const res = await api.get(`/api/validator/${email}`);
      if(res.data === null) {
        toast.warning('E-mail não encontrado. Realize seu cadastro');
        history.push("/codesecurity")
      } else {
        console.log(res.data)
        }
    } else {
      toast.error('Acesse com seu e-mail corporativo');
    }
  

   }

   

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