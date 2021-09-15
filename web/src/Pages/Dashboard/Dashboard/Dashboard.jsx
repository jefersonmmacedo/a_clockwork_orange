import './dashboard.css';
import React, { useContext, useEffect, useState } from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiCalendar, FiEdit, FiTrash2,FiCheckCircle, FiX} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import Modal from 'react-modal'
import { AuthContext } from '../../../Contexts/Auth';
import api from '../../../services/api';

 


export default function Dashboard() {
  const {user, deleteSheduling, findOndeScheduling, oneScheduling} = useContext(AuthContext);
  console.log(oneScheduling)
  const [data, setData] = useState([])
  const history = useHistory();
  const [isOpenModal, setIsOpenModal] = useState(false);

  useEffect(() => {
    async function filterShedulingUser(_idUser, limit){
      const data = {_idUser: user._id, limit: 8}
       const res = await api.post('/api/filter/user', data);

       setData(res.data.result);
       console.log(res.data)
        
   }
   filterShedulingUser()
  }, [])

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
                  <FiUser />
                </div>
                  <button className="button-primary" onClick={handleRedirect}>Fazer Agendamento</button>
                <div className="schedules">
                  <h3>Meus Agendamentos</h3>

                  <table>
                    <thead>
                      <tr>
                        <th>Sede</th>
                        <th>Turno</th>
                        <th>Data</th>
                        <th>Ações</th>
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
                          <button type="button" className="button-table"> <FiEdit /></button>  
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
                  <p>Referente a 07/09/2021. Ver outra data? <FiCalendar /></p>
                  <h5>São Paulo - 83/240</h5>
                  <progress value="83" max="240"></progress>
                  <h5>Santos - 35/40</h5>
                  <progress value="35" max="40"></progress>
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
        <button className="button-primary" onClick={handleShedulingDelete}><FiCheckCircle /> Agendar</button>
        </div>
        </div>
        </Modal>


        <Footer />
      </div>
    </div>
  );
};