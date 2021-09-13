import './dashboard.css';
import React, { useContext } from 'react';
import userScheduling from '../../../assets/images/userScheduling.svg';
import {useHistory} from 'react-router-dom'
import Footer from '../../../Components/Footer/Footer';
import {FiUser, FiCalendar, FiEdit, FiTrash2} from 'react-icons/fi';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import { AuthContext } from '../../../Contexts/Auth';

 


export default function Dashboard() {
  const {user} = useContext(AuthContext);
  console.log(user)
  const history = useHistory();

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
                      <tr>
                        <td>São Paulo</td>
                        <td>Manhã</td>
                        <td>07/09/2021</td>
                        <td><FiTrash2 /> <FiEdit /></td>
                      </tr>

                      <tr>
                        <td>São Paulo</td>
                        <td>Manhã</td>
                        <td>14/09/2021</td>
                        <td><FiTrash2 /> <FiEdit /></td>
                      </tr>

                      <tr>
                        <td>São Paulo</td>
                        <td>Manhã</td>
                        <td>21/09/2021</td>
                        <td><FiTrash2 /> <FiEdit /></td>
                      </tr>

                      <tr>
                        <td>São Paulo</td>
                        <td>Manhã</td>
                        <td>28/09/2021</td>
                        <td><FiTrash2 /> <FiEdit /></td>
                      </tr>
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
        <Footer />
      </div>
    </div>
  );
};