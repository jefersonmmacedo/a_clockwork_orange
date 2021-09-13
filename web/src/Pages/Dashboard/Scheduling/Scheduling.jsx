import './scheduling.css';
import React, { useContext, useState } from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiPlusCircle} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import { AuthContext } from '../../../Contexts/Auth';

 


export default function Scheduling() {
  const history = useHistory();
  const {user, scheduling} = useContext(AuthContext);
  const [location, setLocation] = useState('');
  const [shift, setShift] = useState('');
  const [type, setType] = useState('');
  const [date, setDate] = useState('');
  const [recurrent, setRecurrent] = useState('');

  function handleScheduling () {
    scheduling(location, shift, type, date, user._id, user.name,user.lastname, user.email, user.role, recurrent)
  }

  function handleRedirect() {
   history.push("/dashboard/dashboard")
  }

  function handleSelectLocation(e) {
    setLocation(e.target.value);
  }

  function handleSelectShift(e) {
    setShift(e.target.value);
  }

  function handleSelectType(e) {
    setType(e.target.value);
  }

  function handleSelectRecurrent(e) {
    setRecurrent(e.target.value);
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
                  <p>Olá, {user.name}</p>
                  <FiUser />
                </div>
                  <h3>Fazer Agendamento</h3>
                <div className="schedules">
                <span>Selecione a unidade</span>
                <select defaultValue={location} onChange={handleSelectLocation}>
                  <option value="">Selecione sua unidade</option>
                  <option value="São Paulo">São Paulo</option>
                  <option value="Santos">Santos</option>
                </select>

                <span>Turno</span>
                <select defaultValue={shift} onChange={handleSelectShift}>
                  <option value="">Escolha o turno de trabalho</option>
                  <option value="Manhã">Manhã</option>
                  <option value="Tarde">Tarde</option>
                  <option value="Dia Inteiro">Dia Inteiro</option>
                </select>

                <span>O que deseja Agendar?</span>
                <select defaultValue={type} onChange={handleSelectType}>
                  <option value="">O que deseja agendar?</option>
                  <option value="Estação de trabalho">Estação de trabalho</option>
                  <option value="Sala de Reuniões">Sala de Reuniões</option>
                </select>

                <span>Que tipo de agendamento deseja fazer?</span>
                <select defaultValue={recurrent} onChange={handleSelectRecurrent}>
                  <option value="">Dia único / Recorrente</option>
                  <option value="unic">Dia único</option>
                  <option value="recurrent">Recorrente</option>
                </select>


               
                <div className="text">
                  <p>Esse é o seu primeiro acesso ao sistema,
                    por favor informe seu código de acesso.
                  </p>
                </div>

                <span>Escolha uma data</span>
                <input id="date" type="date" defaultValue={date} onChange={(e) => setDate(e.target.value)}/>


                <p>Deseja adicionar mais um período de dias? <FiPlusCircle />
                  </p>
                 <div className="buttons">

                <button className="button-White" onClick={handleRedirect}>Voltar</button>
                <button className="button-primary" onClick={handleScheduling}>Agendar</button>
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