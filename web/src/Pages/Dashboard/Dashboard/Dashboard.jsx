import './dashboard.css';
import React, { useContext, useEffect, useState } from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import {Link, useHistory} from 'react-router-dom'
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiCalendar, FiEdit, FiTrash2,FiCheckCircle, FiX} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import Modal from 'react-modal'
import { AuthContext } from '../../../Contexts/Auth';
import api from '../../../services/api';
import {format, parseISO} from 'date-fns';

 


export default function Dashboard() {
  const {user, deleteSheduling, findOndeScheduling, oneScheduling} = useContext(AuthContext);
  console.log(oneScheduling)
  const [isOpenModal, setIsOpenModal] = useState(false);
  const history = useHistory();
  const [data, setData] = useState([]);
  const [length, setLength] = useState('');
  const [lengthSantos, setLengthSantos] = useState('');
  const [shift, setShift] = useState('');
  const [type, setType] = useState('');
  const [date, setDate] = useState('');
  const [classCss, setClassCss] = useState('inputs-desable');
 
  const dataHoje = new Date();
  let actualDate = (dataHoje.getDate() < 10 ?  "0" + ((dataHoje.getDate())) : ((dataHoje.getDate() ))) + "/" + (dataHoje.getMonth() < 9? "0" + ((dataHoje.getMonth() + 1)) : ((dataHoje.getMonth() + 1)) )+ "/" + dataHoje.getFullYear()
  
  let dateNew;
  if(date === "") {
  } else {
    dateNew = format(parseISO(date),'dd/MM/yyyy');
  }

  useEffect(() => {
    async function filterShedulingUser(_idUser){
      const data = {_idUser: user._id, limit: 8}
       const res = await api.post('/api/filter/user', data);


       setData(res.data.result);
       console.log(res.data)
        
   }

   async function filterSchedulingSaoPaulo() {
     const scheduling = {location: "São Paulo", type: type === "" ? "Estação de Trabalho" : type, shift: shift === "" ? "Manhã" : shift, date: date === "" ? actualDate : dateNew};
     console.log(scheduling)
     const res = await api.post('/api/filter', scheduling);

      setLength(res.data.length);
       console.log(res.data.length)
   }
 
   async function filterSchedulingSantos() {
    const scheduling = {location: "Santos", type: type === "" ? "Estação de Trabalho" : type, shift: shift === "" ? "Manhã" : shift, date: date === "" ? actualDate : dateNew} ;
    console.log("dados: " + scheduling)
    const res = await api.post('/api/filter', scheduling);

     setLengthSantos(res.data.length);
      console.log(res.data.length)
  }
   filterShedulingUser()
   filterSchedulingSaoPaulo()
   filterSchedulingSantos()
  }, [user._id, type, shift, date, actualDate, dateNew])

  function handleOpenModal() {
    setIsOpenModal(true)
  }

  function handleCloseModal() {
    setIsOpenModal(false)
  }

  function handleDelete(id) {
    findOndeScheduling(id);
    handleOpenModal()
  }
  function handleShedulingDelete() {
    deleteSheduling(oneScheduling._id)
  }

  function handleRedirect() {
   history.push("/dashboard/scheduling")
  }

  function handleRedirectAccount() {
    history.push("/dashboard/account")
   }

  function handleRedirectEdit(_id) {
    history.push(`/dashboard/editscheduling/${_id}`)
   }

   function handleSelectShift(e) {
    setShift(e.target.value);

  }

  function handleSelectType(e) {
    setType(e.target.value);
  }

  function handleEnableInputs(e){
    e.preventDefault() 
    setClassCss('inputs-class')
  }

  function handleDisableInputs(e){
    e.preventDefault() 
    setClassCss('inputs-desable')
  }


   Modal.setAppElement('#root');
  return (
    <div className="container">
      <div className="content">
      <Navbar />
        <div className="dashboard">
         <div className="infos">
         <ImageBody image={userScheduling} alt="User-calendar"/>
            <div className="itens">
                <div className="saudation">
                  <p>Olá, {user.name}</p>
                  <Link to="/dashboard/scheduling" ><FiUser /></Link> 
                </div>
                  <button className="button-primary" onClick={handleRedirect}>Fazer Agendamento</button>
                <div className="schedules">
                  <h3>Meus Agendamentos</h3>

                  <table>
                    <thead>
                      <tr>
                        <th scope="col">Sede</th>
                        <th scope="col">Turno</th>
                        <th scope="col">Data</th>
                        <th scope="col">Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                     {data.map((userData => {
                       return (
                        <tr key={userData._id}>
                        <td>{userData.location}</td>
                        <td>{userData.shift}</td>
                        <td>{userData.date}</td>
                        <td>
                          <div className="buttons-table">
                          <button type="button" className="button-table" onClick={() => {handleRedirectEdit(userData._id)}}> <FiEdit /></button>  
                        <button type="button" className="button-table" onClick={() => {handleDelete(userData._id)}}> <FiTrash2 /></button>  
                          </div>
                          </td>
                      </tr>
                       )
                     }))}
                      
                    </tbody>
                  </table>
                </div>
                <div className="disponible">
                  <h3>Disponibilidade</h3>
                  <p>Referente a {actualDate}. Ver outra data? <button className="button-table" onClick={handleEnableInputs}><FiCalendar /></button> </p>
                  <div className={classCss}>
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

                <span>Escolha uma data</span>
                <input id="date" type="date" defaultValue={date} onChange={(e) => setDate(e.target.value)}/>

                <button className="button-table" onClick={handleDisableInputs}><FiX/></button> 
                  </div>
                  <h5>São Paulo - {length}/240</h5>
                  <progress value={length} max="240"></progress>
                  <h5>Santos - {lengthSantos}/40</h5>
                  <progress value={lengthSantos} max="40"></progress>
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
        <h3>Apagar agendamento?</h3>
       
          <div className="itensModal">
          <p>Escritório: {oneScheduling.location}</p>
        <p>Tipo: {oneScheduling.type}</p>
        <p>Turno: {oneScheduling.shift}</p>
        <p>Data: {oneScheduling.date}</p>
        <p>Dia da Semana: {oneScheduling.day}</p>
         </div>
        
          
        <div className="buttons-modal">
        <button className="button-White" onClick={handleCloseModal}>Cancelar</button>
        <button className="button-primary" onClick={handleShedulingDelete}><FiCheckCircle /> Apagar</button>
        </div>
        </div>
        </Modal>


        <Footer />
      </div>
    </div>
  );
};