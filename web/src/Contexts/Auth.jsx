import { createContext, useState } from "react";
import {useHistory, Red} from 'react-router-dom'
import {toast} from 'react-toastify';
import api from "../services/api";

export const AuthContext = createContext({});

function AuthProvider({children}) {
    const [userEmail, setUserEmail] = useState('')
    const history = useHistory();

    async function validateEmail(email) {
        const res = await api.get(`/api/validator/${email}`);
        if(res.data === null) {
          toast.warning('Seja bem vindo. Realize seu cadastro');
        } else {
            toast.success(res.data.email);
            setUserEmail(res.data.email)
          }
    }

    return (
        <AuthContext.Provider
        value={{
            validateEmail,
            userEmail
        }}
        >
         {children}   
        </AuthContext.Provider>
    )
}

export default AuthProvider;