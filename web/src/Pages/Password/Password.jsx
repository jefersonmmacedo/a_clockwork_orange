import './password.css';
import React, { useContext, useState } from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';
import { AuthContext } from '../../Contexts/Auth';
import { toast } from 'react-toastify';



export default function Password() {
 const [password, setPassword] = useState('');
 const {email, login} = useContext(AuthContext);

 function handleLogin() {
   if(password !== '') {
     console.log(email)
    login(email,password)
   } else {
     toast.error('Favor preencher o campo senha')
   }
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
                <input type="password" placeholder="Digite sua senha" defaultValue={password} onChange={(e) => setPassword(e.target.value)}/>
                <button className="button-primary" onClick={handleLogin}>Entrar</button>
                <span>Esqueci a Senha</span>
            </div>
          </div>
           </div>
           <Footer />
      </div>
    </div>
  );
};