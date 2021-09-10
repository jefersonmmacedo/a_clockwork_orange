import {FiLogOut} from 'react-icons/fi';
import logo from '../../assets/images/logo.svg';
import './navbar.css'


export default function Navbar(){
    return (
        <div className="navbar">
            <img src={logo} alt="Logo FCamara" />
            <FiLogOut size={24} color="#36357E"/>

        </div>
    )
}