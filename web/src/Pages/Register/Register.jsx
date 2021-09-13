import './register.css';
import React, { useContext, useState } from 'react';
import logoAside2 from '../../assets/images/logoAside2.svg';
import smartphoneAside from '../../assets/images/smartphoneAside.svg';
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';
import { AuthContext } from '../../Contexts/Auth';
import { toast } from 'react-toastify';



export default function Register() {
  const {createUser, newEmail} = useContext(AuthContext)
  const [name, setName] = useState('');
  const [lastname, setLastname] = useState('')
  const [role, setRole] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');

  function handleCreateUser() {
    if(passwordConfirm === password) {
      createUser(name, lastname, newEmail, role, password)
    } else {
      toast.error('As senhas não conferem.')
    }
  }

  function handleSelectRole(e) {
    setRole(e.target.value);
    console.log(e.target.value)
  }

  return (
    <div className="container">
      <div className="content">
        <div className="register">
          <div className="infos">
          <ImageBody image={smartphoneAside} alt="image-smartphoneUser"/>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />

                <span>Nome</span>
                <input type="text" placeholder="Digite seu nome" defaultValue={name} onChange={(e) => setName(e.target.value)}/>
                <span>Sobrenome</span>
                <input type="text" placeholder="Digite seu sobrenome" defaultValue={lastname} onChange={(e) => setLastname(e.target.value)}/>
                <span>Email</span>
                <input type="text" placeholder="Digite seu nome completo" defaultValue={newEmail} />
                 <span>Qual é a sua função?</span>
                <select defaultValue={role} onChange={handleSelectRole}>
                  <option value="">Selecione Sua Função</option>
                  <option value="Fullstack Developer Jr">Fullstack Developer Jr</option>
                  <option value="Fullstack Developer Pleno">Fullstack Developer Pleno</option>
                  <option value="Fullstack Developer Senior">Fullstack Developer Senior</option>
                  <option value="Front-end Developer Jr">Front-end Developer Jr</option>
                  <option value="Front-end Developer Pleno">Front-end Developer Pleno</option>
                  <option value="Front-end Developer Senior">Front-end Developer Senior</option>
                  <option value="Back-end Developer Jr">Back-end Developer Jr</option>
                  <option value="Back-end Developer Pleno">Back-end Developer Pleno</option>
                  <option value="Back-end Developer Senior">Back-end Developer Senior</option>
                  <option value="Mobile Developer Jr">Mobile Developer Jr</option>
                  <option value="Mobile Developer Pleno">Mobile Developer Pleno</option>
                  <option value="Mobile Developer Senior">Mobile Developer Senior</option>
                  <option value="UI/UX Designer Jr">UI/UX Designer Jr</option>
                  <option value="UI/UX Designer Pleno">UI/UX Designer Pleno</option>
                  <option value="UI/UX Designer Senior">UI/UX Designer Senior</option>
                  <option value="Scrum Master">Scrum Master</option>
                  <option value="Product Owner">Product Owner</option>
                  <option value="Agile Coach">Agile Coach</option>
                  <option value="Gerente de Projetos">Gerente de Projetos</option>
                  <option value="QA">QA</option>
                </select>
                <span>Senha</span>
                <input type="password" placeholder="Senha" defaultValue={password} onChange={(e) => setPassword(e.target.value)}/>
                <span>Confirmar senha</span>
                <input type="password" placeholder="Confirmar senha" defaultValue={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)}/>
                <button className="button-primary" onClick={handleCreateUser}>Criar conta</button>
            </div>
          </div>
          </div>
          <Footer />
      </div>
    </div>
  );
};