import './editScheduling.css';
import React, { useContext, useEffect, useState } from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiPlusCircle, FiX, FiCheckCircle} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import { useParams } from 'react-router';
import api from '../../../services/api';
import Modal from 'react-modal';
import {useHistory} from 'react-router-dom';
import {format, parseISO} from 'date-fns';
import { AuthContext } from '../../../Contexts/Auth';
 


export default function EditScheduling() {
  const {schedulingUpdate} = useContext(AuthContext)
  const history = useHistory();
  const {_id} = useParams()
  const [scheduling, setScheduling] = useState([]);
  const [location, setLocation] = useState('');
  const [shift, setShift] = useState('');
  const [type, setType] = useState('');
  const [date, setDate] = useState('');
  const [isOpenModal, setIsOpenModal] = useState(false);
  let dateNew;

  useEffect(() => {
    async function filterShedulingUser(){
       const res = await api.get(`/api/scheduling/${_id}`);
       setScheduling(res.data)
        
   }
   filterShedulingUser()
  }, [_id]);
 
if(date === "") {
} else if(date !== scheduling.date) {
  dateNew = format(parseISO(date),'dd/MM/yyyy');
}

const day = parseISO(date) ;
let bdDay = day.getDay();
switch(bdDay) {
    case 1:
    bdDay = 'Segunda-Feira';
    break;
    case 2:
    bdDay = 'Terça-Feira';
    break;
    case 3:
    bdDay = 'Quarta-Feira';
    break;
    case 4:
    bdDay = 'Quinta-Feira';
    break;
    case 5:
    bdDay = 'Sexta-Feira';
    break;
    case 6:
    bdDay = 'Sábado';
    break;
    case 0:
    bdDay = 'Domingo';
    break;
    default:
    bdDay = 'Dia não encontrado';
}




  function handleOpenModal() {
      setIsOpenModal(true);

    setLocation(location === "" ? scheduling.location : location);
    setShift(shift === "" ? scheduling.shift : shift);
    setType(type === "" ? scheduling.type : type);
  

  }

  function handleCloseModal() {
    setIsOpenModal(false)
  }

  function handleSchedulingUpdate() {
   const data = {_id: scheduling._id, location, shift, type, date: date === "" ? scheduling.date : dateNew, day:bdDay === "Dia não encontrado" ? scheduling.day : bdDay }
   schedulingUpdate(data)
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

 
  Modal.setAppElement('#root');

  return (
    <div className="container">
      <div className="content">
        <Navbar />
        <div className="editScheduling">
         <div className="infos">
         <ImageBody image={userScheduling} alt='user-scheduling' />
            <div className="itens">
                <div className="saudation">
                  <p>Olá, Jeferson Macedo</p>
                  <FiUser />
                </div>
                  <h3>Editar Agendamento</h3>
               

                  <div className="schedules">
                <span>Selecione a unidade</span>
                <select defaultValue={scheduling.location} onChange={handleSelectLocation}>
                  <option value={scheduling.location} >{scheduling.location} </option>
  
                  <option value={scheduling.location === 'São Paulo' ? "Santos" : "São Paulo"}>{scheduling.location === 'São Paulo' ? "Santos" : "São Paulo"}</option>
                </select>

                <span>O que deseja Agendar?</span>
                <select defaultValue={scheduling.type} desabled onChange={handleSelectType}>
                  <option value={scheduling.type}>{scheduling.type}</option>
                </select>

                <span>Turno / Período</span>
                {scheduling.type === 'Estação de trabalho' ?
                <select defaultValue={scheduling.shift} onChange={handleSelectShift}>
                  <option value={scheduling.shift}>{scheduling.shift}</option>
                  <option value="">Escolha outro Turno</option>
                  <option value="Manhã">Manhã</option>
                  <option value="Tarde">Tarde</option>
                  <option value="Dia Inteiro">Dia Inteiro</option>
                </select>
                :
                <select defaultValue={scheduling.shift} onChange={handleSelectShift}>
                <option value={scheduling.shift}>{scheduling.shift}</option>
                <option value="">Escolha outro período</option>
                <option value="08h às 10h">08h às 10h</option>
                <option value="10h às 12h">10h às 12h</option>
                <option value="12h às 14h">12h às 14h</option>
                <option value="14h às 16h">14h às 16h</option>
                <option value="16h às 18h">16h às 18h</option>
                </select>
                }

                <span>Nova data (Data atual: {scheduling.date})</span>
                <input id="date" type="date" defaultValue={date} onChange={(e) => setDate(e.target.value)}/>


                <p>Deseja adicionar mais um período de dias? <FiPlusCircle />
                  </p>
                 <div className="buttons">

                <button className="button-White" onClick={handleRedirect}>Voltar</button>
                <button className="button-primary" onClick={handleOpenModal}>Agendar</button>
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
          <h3>Editar agendamento?</h3>
          <div className="itensModal">
  
            <p>Escritório: {location === "" ? scheduling.location : location}</p>
            <p>Tipo: {type === "" ? scheduling.type : type}</p>
            <p>Turno: {shift === "" ? scheduling.shift : shift}</p>
            <p>Data: {date === "" ? scheduling.date : dateNew}</p>
            <p>Dia da Semana: {bdDay === "Dia não encontrado" ? scheduling.day : bdDay}</p>
          </div>

          
        <div className="buttons-modal">
        <button className="button-White" onClick={handleCloseModal}>Cancelar</button>
        <button className="button-primary" onClick={handleSchedulingUpdate}><FiCheckCircle /> Agendar</button>
        </div>
        </div>
        </Modal>


        <Footer />
      </div>
    </div>
  );
};