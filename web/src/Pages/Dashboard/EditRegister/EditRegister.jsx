import './editRegister.css';
import React, { useState } from 'react';
import editUser from '../../../assets/images/editUser.svg';
import {useHistory} from 'react-router-dom'
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import Footer from '../../../Components/Footer/Footer';



export default function EditRegister() {
 
  return (
    <div className="container">
      <div className="content">
        <Navbar />
        <div className="editRegister">
          <div className="infos">
          <ImageBody image={editUser} alt="User-register"/>
            <div className="itens">
              <h3>Atualizar Cadastro</h3>
                <span>Nome Completo</span>
                <input type="text" placeholder="Digite seu nome completo"/>
                <span>Email</span>
                <input type="text" placeholder="Digite seu email"/>
                 <span>Qual é a sua função?</span>
                <select name="" id="">
                  <option value="">Selecione Sua Função</option>
                  <option value="">Fullstack Developer Jr</option>
                  <option value="">Fullstack Developer Pleno</option>
                  <option value="">Fullstack Developer Senior</option>
                  <option value="">Front-end Developer Jr</option>
                  <option value="">Front-end Developer Pleno</option>
                  <option value="">Front-end Developer Senior</option>
                  <option value="">Back-end Developer Jr</option>
                  <option value="">Back-end Developer Pleno</option>
                  <option value="">Back-end Developer Senior</option>
                  <option value="">Mobile Developer Jr</option>
                  <option value="">Mobile Developer Pleno</option>
                  <option value="">Mobile Developer Senior</option>
                  <option value="">UI/UX Designer</option>
                  <option value="">Scrum Master</option>
                  <option value="">Product Owner</option>
                  <option value="">Agile Coacho</option>
                  <option value="">Gerente de Projetos</option>
                  <option value="">Analista de Dados</option>
                  <option value="">Analista de Dados</option>
                  <option value="">Analista de Dados</option>
                  <option value="">Analista de Dados</option>
                </select>
                <span>Senha</span>
                <input type="text" placeholder="Senha"/>
                <span>Confirmar senha</span>
                <input type="text" placeholder="Confirmar senha"/>
                <button className="button-primary">Atualizar Cadastro</button>
            </div>
          </div>
        </div>
        <Footer />
      </div>
    </div>
  );
};