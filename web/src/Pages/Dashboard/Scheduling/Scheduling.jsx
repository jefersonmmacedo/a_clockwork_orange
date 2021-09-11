import './scheduling.css';
import React from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiPlusCircle} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';

 


export default function Scheduling() {
  const history = useHistory();

  function handleRedirect() {
   history.push("/password")
  }

  return (
    <div className="container">
      <div className="content">
        <Navbar />
        <div className="Scheduling">
         <div className="infos">
           <ImageBody image={userScheduling} alt='user-scheduling' />
            <div className="itens">
                <div className="saudation">
                  <p>Olá, Jeferson Macedo</p>
                  <FiUser />
                </div>
                  <h3>Fazer Agendamento</h3>
                <div className="schedules">
                <span>Selecione a unidade</span>
                <select name="" id="">
                  <option value="">Selecione sua unidade</option>
                  <option value="">São Paulo</option>
                  <option value="">Santos</option>
                </select>

                <span>O que deseja Agendar?</span>
                <select name="" id="">
                  <option value="">Estação de trabalho / Sala de Reuniões</option>
                  <option value="">Estação de trabalho</option>
                  <option value="">Sala de Reuniões</option>
                </select>

                <span>Que tipo de agendamento deseja fazer?</span>
                <select name="" id="">
                  <option value="">Dia único / Recorrente</option>
                  <option value="">Dia único</option>
                  <option value="">Recorrente</option>
                </select>


                <span>Turno</span>
                <select name="" id="">
                  <option value="">Manhã / Tarde / Dia inteiro</option>
                  <option value="">Manhã</option>
                  <option value="">Tarde</option>
                  <option value="">Dia Inteiro</option>
                </select>
                <div className="text">
                  <p>Esse é o seu primeiro acesso ao sistema,
                    por favor informe seu código de acesso.
                  </p>
                </div>

                <span>Escolha uma data</span>
                <input id="date" type="date"/>


                <p>Deseja adicionar mais um período de dias? <FiPlusCircle />
                  </p>
                 <div className="buttons">

                <button className="button-White" onClick={handleRedirect}>Voltar</button>
                <button className="button-primary" onClick={handleRedirect}>Agendar</button>
                </div>
                 </div>
            </div>
          </div>
        </div>
        <Footer />
      </div>
    </div>
  );
};