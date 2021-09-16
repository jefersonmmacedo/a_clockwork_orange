import './passwordRecuperation.css';
import React, { useContext, useState } from 'react';
import work from '../../assets/images/work.svg';
import logoAside2 from '../../assets/images/logoAside2.svg';
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';
import { AuthContext } from '../../Contexts/Auth';
import { toast } from 'react-toastify';



export default function PasswordRecuperation() {
 const [password, setPassword] = useState('');
 const [passwordConfirm, setPasswordConfirm] = useState('');
 const {id, name, email, role, lastname, updatePassword} = useContext(AuthContext);

 function handleLogin() {
   if(password === passwordConfirm) {
     console.log(id)
     updatePassword(id, name, lastname, email, role ,password)
   } else {
     toast.error('Favor preencher o campo senha')
   }
 }

  return (
    <div className="container">
      <div className="content">
        <div className="passwordRecuperation">
          <div className="infos">
          <ImageBody image={work} alt="image-computer"/>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <span>Senha</span>
                <input type="password" placeholder="Digite sua senha" defaultValue={password} onChange={(e) => setPassword(e.target.value)}/>
                <span>Confirmar Senha</span>
                <input type="password" placeholder="Digite sua senha" defaultValue={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)}/>
                <button className="button-primary" onClick={handleLogin}>Entrar</button>
            </div>
          </div>
           </div>
           <Footer />
      </div>
    </div>
  );
};