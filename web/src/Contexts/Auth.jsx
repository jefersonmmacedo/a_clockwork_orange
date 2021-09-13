import { createContext, useEffect, useState } from "react";
import { useHistory } from "react-router";

import {toast} from 'react-toastify';
import api from "../services/api";

export const AuthContext = createContext({});

function AuthProvider({children}) {
    const history = useHistory();
    const [user, setUser] = useState(null);
    const [newEmail, setNewEmail] = useState('');
    const [email, setEmail] = useState('');
    const [codeSecurity, setCodeSecurity] = useState('');
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        function loadStorage() {
            const storageUser = localStorage.getItem("fcamara");

        if(storageUser) {
            setUser(JSON.parse(storageUser));
            setLoading(false);
        }
        setLoading(false);
        }
        loadStorage();
    },[])
  

    async function validateEmail(email) {
        const res = await api.get(`/api/validator/${email}`);
        if(res.data === null) {
             setNewEmail(email)
             toast.warning('Seja bem vindo. Realize seu cadastro');
             history.push('/codesecurity');
        } else {
            toast.success(`Seja bem-vindo de volta, entre com sua senha por favor!`);
            setEmail(res.data)
            console.log(res.data)
            history.push('/password')
          }
    }

    async function validateCode(code) {
        const res = await api.get(`/api/securitycode/${code}`);
        console.log(res.data.securityCode)
        if(res.data === null) {
            toast.warning('Códifo Inválido ou Expirado');
          } else {
             setCodeSecurity(res.data)
              history.push('/register')
          }
    }

    async function createUser(name, email, role, password) {
        const dataUser = {name, email, role, password}
        const res = await api.post('/api/user', dataUser);
        console.log(res.data)
        if(res.status === 200) {
            toast.success('Cadastro efetuado com sucesso. Efetue seu login');
            history.push('/login')
          } else {
            toast.error('Ops. Ocorreu um erro.');
          }
    }

    async function login(email, password) {
        const data = {email, password}
        const res = await api.post('api/user/login', data);
        
        if(res.data.error === 'Invalid password.') {
            toast.error('Senha incorreta.');
        } else {
            toast.success(`Seja bem-vindo(a), ${res.data.name}`);
            setUser(res.data);
            storageUser(res.data);
            setLoading(false)
            console.log(res.data)

        }
    }

    async function scheduling(location, shift, type, date, _idUser, name,lastname, email, role) {
        const dateNew = date.split('-').reverse().join('/');
        const Day = new Date(date) ;
        const dateDay = Day.getDay() + 1;
        let bdDay = '';
        switch(dateDay) {
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
            case 7:
            bdDay = 'Domingo';
            break;
        }
        const data = {location, shift, type, date: dateNew, day: bdDay, _idUser, name,lastname, email, role};
        

       const res = await api.post('/api/scheduling', data);
       console.log(res.data)
       if(res.status === 200) {
           toast.success('Agendamento efetuado com sucesso. Efetue seu login');
           history.push('/dashboard/dashboard')
         } else {
           toast.error('Ops. Ocorreu um erro.');
         }
    }

    function storageUser(data) {
        localStorage.setItem("fcamara", JSON.stringify(data));
    }

    function logout() {
        localStorage.removeItem("fcamara")
        setUser(null)
    }

    return (
        <AuthContext.Provider
        value={{
            validateEmail,
            validateCode,
            createUser,
            login,
            storageUser,
            logout,
            scheduling,
            email,
            newEmail,
            codeSecurity,
            signed: !!user,
            user,
            loading
        }}
        >
         {children}   
        </AuthContext.Provider>
    )
}

export default AuthProvider;