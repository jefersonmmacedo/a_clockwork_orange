import './scheduling.css';
import React, { useContext, useEffect, useState } from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiPlusCircle, FiX, FiCheckCircle} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import Modal from 'react-modal'
import { AuthContext } from '../../../Contexts/Auth';
import api from '../../../services/api';
import {format, parseISO} from 'date-fns';




export default function Scheduling() {
  const history = useHistory();
  const {user,dataUser, scheduling, schedulingCreate} = useContext(AuthContext);
  const [location, setLocation] = useState('');
  const [shift, setShift] = useState('');
  const [type, setType] = useState('');
  const [date, setDate] = useState('');
  const [recurrent, setRecurrent] = useState('unic');
  const [isOpenModal, setIsOpenModal] = useState(false);
  const [length, setLength] = useState('');
  const [buttonCss, setButtonCss] = useState('button-primary');
  const [message, setMessage] = useState('disponible');



let dateNew;
if(date === "") {
} else {
  dateNew = format(parseISO(date),'dd/MM/yyyy');
}
const day = parseISO(date) ;

const daySem = day.getDay();



useEffect(() => {
  async function filterSchedulingSaoPaulo() {
    const scheduling = {location: location, type: type, shift: shift, date:dateNew};
    console.log(scheduling)
    const res = await api.post('/api/filter', scheduling);

     setLength(res.data.length);
      console.log(res.data.length);

        if(length > 3 ) {
        setButtonCss("button-primary-disabled")
        setMessage('indisponible')
      }else if (daySem === 0 || daySem === 6){
        setButtonCss()
        setMessage("Weekend")
      } else{
        setButtonCss('button-primary-disabled')
        setMessage("disponible")
      }
  }

  filterSchedulingSaoPaulo()

}, [location, type, date, shift, dateNew, length])

  function handleOpenModal() {
    setIsOpenModal(true)
  }

  function handleCloseModal() {
    setIsOpenModal(false)
  }

  function handleScheduling () {
    scheduling(location, shift, type, date, user._id, user.name,user.lastname, user.email, user.role, recurrent);
   handleOpenModal()
  }

  function handleSchedulingCreate() {
    schedulingCreate()
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

  Modal.setAppElement('#root');
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

                <span>O que deseja Agendar?</span>
                <select defaultValue={type} onChange={handleSelectType}>
                  <option value="">O que deseja agendar?</option>
                  <option value="Estação de trabalho">Estação de trabalho</option>
                  <option value="Sala de Reuniões">Sala de Reuniões</option>
                </select>

                <span>Turno / Período</span>
                {type === 'Estação de trabalho' ?
                <select defaultValue={shift} onChange={handleSelectShift}>
                  <option value="">Escolha o turno de trabalho</option>
                  <option value="Manhã">Manhã</option>
                  <option value="Tarde">Tarde</option>
                  <option value="Dia Inteiro">Dia Inteiro</option>
                </select>
                :
                <select defaultValue={shift} onChange={handleSelectShift}>
                <option value="">Escolha o período</option>
                <option value="08h às 10h">08h às 10h</option>
                <option value="10h às 12h">10h às 12h</option>
                <option value="12h às 14h">12h às 14h</option>
                <option value="14h às 16h">14h às 16h</option>
                <option value="16h às 18h">16h às 18h</option>
                </select>
                }


                <span>Que tipo de agendamento deseja fazer?</span>
                {type === '' ? 
                 <select defaultValue={recurrent} onChange={handleSelectRecurrent}>
                 <option value="">Tipo de agendamento</option>
               </select>
             
               :type === 'Estação de trabalho' ?
               <select defaultValue={recurrent} onChange={handleSelectRecurrent}>
                <option value="">Escolha: Dia único ou Recorrente</option>
                <option value="unic">Dia único</option>
                <option value="recurrent">Recorrente</option>
                </select>
              :
              <select defaultValue={recurrent} onChange={handleSelectRecurrent}>
                  <option value="unic">Dia único</option>
                </select>
                }
               

               

                {message === 'disponible' ?
                
                <div className="text">
                  <p>Agende o mesmo dia da semana por 4 vezes consecutivas.</p>
                </div>
                : message === 'Weekend' ?
                <div className="text-Weekend">
                <p>Nosso horário de funcionemento: <br />
                  Segunda à Sexta - 08h às 18h <br />
                  Agende outra data por favor!.</p>
              </div>
                :
                <div className="text-warning">
                  <p>Esta data chegou a sua lotação máxima. <br /> Ou está agendando  Favor escolher outra data.</p>
                </div>
              }

                  <span>Escolha uma data</span>
                <input id="date" type="date" defaultValue={date} onChange={(e) => setDate(e.target.value)}/>

                <p>Deseja adicionar mais um período de dias? <FiPlusCircle />
                  </p>
                 <div className="buttons">

                <button className="button-White" onClick={handleRedirect}>Voltar</button>
                <button className={buttonCss} onClick={handleScheduling}>Agendar</button>
                </div>
                 </div>
            </div>
          </div>
        </div>

        <Modal isOpen={isOpenModal} onRequestClose={handleCloseModal}
        overlayClassName="react-modal-overlay"
        className="react-modal-content">
          <button type="button" className="react-modal-button" onClick={handleCloseModal}>
          <FiX />
          </button>
          <div className="content-modal">
        <h3>Confirmar agendamento?</h3>
          {dataUser.recurrent === 'unic'?
          <div className="itensModal" key={dataUser._id}>
          <p>Escritório: {dataUser.location}</p>
        <p>Tipo: {dataUser.type}</p>
        <p>Turno: {dataUser.shift}</p>
        <p>Data: {dataUser.date}</p>
        <p>Dia da Semana: {dataUser.day}</p>
       </div>
       :
        dataUser.type === "Sala de Reuniões" ?
        <div className="itensModal" key={dataUser._id}>
        <p>Escritório: {dataUser.location}</p>
      <p>Tipo: {dataUser.type}</p>
      <p>Turno: {dataUser.shift}</p>
      <p>Data: {dataUser.date}</p>
      <p>Dia da Semana: {dataUser.day}</p>
     </div>
     :
        dataUser.map((user) => {
          return (
              <div className="itensModal" key={user._id}>
                <p>Escritório: {user.location}</p>
              <p>Tipo: {user.type}</p>
              <p>Turno: {user.shift}</p>
              <p>Data: {user.date}</p>
              <p>Dia da Semana: {user.day}</p>
             </div>
            )
        })}
          
        <div className="buttons-modal">
        <button className="button-White" onClick={handleCloseModal}>Cancelar</button>
        <button className="button-primary" onClick={handleSchedulingCreate}><FiCheckCircle /> Agendar</button>
        </div>
        </div>
        </Modal>
        <Footer />
      </div>
    </div>
  );
};