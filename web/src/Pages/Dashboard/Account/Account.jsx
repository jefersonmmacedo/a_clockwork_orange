import './account.css';
import editUser from '../../../assets/images/editUser.svg';
import Navbar from '../../../Components/Navbar/Navbar';
import ImageBody from '../../../Components/ImageBody/ImageBody';
import Footer from '../../../Components/Footer/Footer';
import { useContext } from 'react';
import { AuthContext } from '../../../Contexts/Auth';
import { useHistory } from 'react-router';



export default function Account() {
  const {user} = useContext(AuthContext);
  console.log(user)
  const history = useHistory();

  function handleRedirect() {
    history.push("/dashboard/editRegister")
   }
 
  return (
    <div className="container">
      <div className="content">
        <Navbar />
        <div className="account">
          <div className="infos">
          <ImageBody image={editUser} alt="User-register"/>
            <div className="itens">
              <h3>Perfil</h3>
                <span><strong>Nome:</strong> {user.name} {user.lastname}</span> 
                <span><strong>Email:</strong> {user.email}</span>
                <span><strong>Função:</strong> {user.role}</span>
            <button className="button-primary" onClick={handleRedirect}>Editar Cadastro</button>
            </div>

          </div>
        </div>
        <Footer />
      </div>
    </div>
  );
};