import { createContext, useState } from "react";
import { useHistory } from "react-router";

import {toast} from 'react-toastify';
import api from "../services/api";

export const AuthContext = createContext({});

function AuthProvider({children}) {
    const history = useHistory();
    const [user, setUser] = useState('');
    const [newEmail, setNewEmail] = useState('');
    const [email, setEmail] = useState('');
    const [codeSecurity, setCodeSecurity] = useState('');
  

    async function validateEmail(email) {
        const res = await api.get(`/api/validator/${email}`);
        if(res.data === null) {
             setNewEmail(email)
             toast.warning('Seja bem vindo. Realize seu cadastro');
             history.push('/codesecurity');
        } else {
            toast.success(`Olá, ${res.data.user}, entre com sua senha por favor!`);
            setEmail(res.data.email)
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

    return (
        <AuthContext.Provider
        value={{
            validateEmail,
            validateCode,
            createUser,
            email,
            newEmail,
            codeSecurity,
            user
        }}
        >
         {children}   
        </AuthContext.Provider>
    )
}

export default AuthProvider;