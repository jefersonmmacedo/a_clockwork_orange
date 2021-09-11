import { createContext, useState } from "react";
import {toast} from 'react-toastify';

export const AuthContext = createContext({});

function AuthProvider({children}) {


    return (
        <AuthContext.Provider
        value={{}}
        >
         {children}   
        </AuthContext.Provider>
    )
}

export default AuthProvider;