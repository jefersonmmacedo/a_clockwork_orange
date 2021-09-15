import './editRegister.css';
import editUser from '../../../assets/images/editUser.svg';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import {FiX, FiCheckCircle} from 'react-icons/fi'
import Footer from '../../../Components/Footer/Footer';
import { useContext, useState } from 'react';
import { AuthContext } from '../../../Contexts/Auth';
import {toast} from 'react-toastify'
import Modal from 'react-modal'



export default function EditRegister() {
  const {user, updateUser} = useContext(AuthContext);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [lastname, setLastname] = useState('')
  const [role, setRole] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [isOpenModal, setIsOpenModal] = useState(false);

  function handleUpdateUser() {
    
    updateUser(user._id,
      name,
      lastname,
      email,
      role,
      password,
   )
  }

  function handleOpenModal() {
    if(password === '' && passwordConfirm === '') {
      toast.warning('Favor preencher a senha')
    } else if(passwordConfirm !== password) {
      toast.warning('Senhas não conferem')
    } else {
      setIsOpenModal(true);
      setName(name === "" ? user.name : name);
      setLastname(lastname === "" ? user.lastname : lastname);
      setEmail(email === "" ? user.email : email);
      setRole(role === "" ? user.role : role);
      setPassword(password === "" ? user.password : password);
    }
    
  }

  function handleCloseModal() {
    setIsOpenModal(false)
  }


  function handleSelectRole(e) {
    setRole(e.target.value);
    console.log(e.target.value)
  }


 
  return (
    <div className="container">
      <div className="content">
        <Navbar />
        <div className="editRegister">
          <div className="infos">
          <ImageBody image={editUser} alt="User-register"/>
            <div className="itens">
              <h3>Atualizar Cadastro</h3>
                <span>Nome</span>
                <input type="text" placeholder="Digite seu nome completo" defaultValue={user.name} onChange={(e) => setName(e.target.value === "" ? user.name : name)}/>
                <span>Sobrenome</span>
                <input type="text" placeholder="Digite seu nome completo" defaultValue={user.lastname} onChange={(e) => setLastname(e.target.value)}/>
                <span>Email</span>
                <input type="text" placeholder="Digite seu email" defaultValue={user.email} onChange={(e) => setEmail(e.target.value)}/>
                 <span>Qual é a sua função?</span>
                <select defaultValue={user.role} onChange={handleSelectRole}>
                <option value={user.role}>{user.role}</option>
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
                <button className="button-primary" onClick={handleOpenModal}>Atualizar Cadastro</button>
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
            <p>Nome: {name === "" ? user.name : name} {lastname === "" ? user.lastname : lastname}</p>
            <p>Email: {email === "" ? user.email : email}</p>
            <p>Função: {role === "" ? user.role : role}</p>
            <p>Senha: ********</p>
  
          </div>
        
          
        <div className="buttons-modal">
        <button className="button-White" onClick={handleCloseModal}>Cancelar</button>
        <button className="button-primary" onClick={handleUpdateUser}><FiCheckCircle /> Agendar</button>
        </div>
        </div>
        </Modal>
        <Footer />
      </div>
    </div>
  );
};