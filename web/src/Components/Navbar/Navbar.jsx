import { useContext } from 'react';
import {FiLogOut} from 'react-icons/fi';
import logo from '../../assets/images/logo.svg';
import { AuthContext } from '../../Contexts/Auth';
import {Link} from 'react-router-dom'
import './navbar.css'


export default function Navbar(){
    const {logout} = useContext(AuthContext);

    function handleLogout() {
        logout()
    }
    return (
        <div className="navbar">
            <Link to="/">
            <img src={logo} alt="Logo FCamara" />
            </Link>
            <button className="button-none" onClick={handleLogout}>
            <FiLogOut size={24} color="#36357E"/>
            </button>

        </div>
    )
}