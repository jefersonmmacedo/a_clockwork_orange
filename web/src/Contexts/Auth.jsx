import { createContext, useEffect, useState } from "react";
import { useHistory } from "react-router";
import {format, parseISO} from 'date-fns';
import {toast} from 'react-toastify';
import api from "../services/api";

export const AuthContext = createContext({});

function AuthProvider({children}) {
    const history = useHistory();
    const [user, setUser] = useState(null);
    const [newEmail, setNewEmail] = useState('');
    const [email, setEmail] = useState('');
    const [codeSecurity, setCodeSecurity] = useState('');
    const [loading, setLoading] = useState(true);
    const [dataUser, setDataUser] = useState([])

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

    async function createUser(name, lastname, email, role, password) {
        const dataUser = {name, lastname, email, role, password}
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
 
    async function scheduling(location, shift, type, date, _idUser, name,lastname, email, role, recurrent) {
        const list = {location, shift, type, date, _idUser, name,lastname, email, role, recurrent}
        console.log(list)
        const dateNew = format(parseISO(date),'dd.MM.yyyy');
        const day = parseISO(date) ;
        let bdDay = day.getDay();
        switch(bdDay) {
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
            case 0:
            bdDay = 'Domingo';
            break;
            default:
            bdDay = 'Dia não encontrado';
        }

        if(recurrent === 'recurrent') {
          
            
            let periodos = [];
            
            for (var i = 0; i < 4; i++) {
                if (i === 0) {
                    periodos.push(day);
                } else {
                    let dataSomada = new Date(new Date().setDate(periodos[i - 1].getDate() + 7));
                    periodos.push(dataSomada);
                }
            }
           const data = [];

           periodos.forEach( async (periodo) => {
                data.push({
                    location,
                    shift,
                    type,
                    date: (periodo.getDate() < 10 ?  "0" + ((periodo.getDate())) : ((periodo.getDate() ))) + "/" + (periodo.getMonth() < 9? "0" + ((periodo.getMonth() + 1)) : ((periodo.getMonth() + 1)) )+ "/" + periodo.getFullYear(),
                    day: bdDay,
                    _idUser,
                    name,
                    lastname,
                    email,
                    role,
                    recurrent
                })
            })
            setDataUser(data);
            console.log(data)

        } else {
            const data = {location, shift, type, date: dateNew, day: bdDay, _idUser, name,lastname, email, role, recurrent};
            setDataUser(data);
            console.log(data);
        }   
        filterSheduling(location, type, shift, dateNew);
    }

    async function schedulingCreate() {
        
        
        if(dataUser.length === 4) {
            for(let i = 0; i < 4; i++) {
                const res = await api.post('/api/scheduling', dataUser[i]);
                 if(res.status === 200) {
                   toast.success('Agendamentos efetuados com sucesso!');
                    history.push('/dashboard/dashboard')
                  } else {
                     toast.error('Ops. Ocorreu um erro.');
                  }
            }
        } else{
            const res = await api.post('/api/scheduling', dataUser);
            if(res.status === 200) {
               toast.success('Agendamentos efetuados com sucesso!');
                history.push('/dashboard/dashboard')
              } else {
                 toast.error('Ops. Ocorreu um erro.');
              }
        }
    }

    async function filterSheduling(location, type, shift, date){
        const data = {location, type, shift, date}
        console.log(data)
         const res = await api.post('/api/filter', data);

         const dados = res.data.result;
         dados.forEach(dado => {
             console.log(dado.date)
         })
         
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
            schedulingCreate,
            email,
            newEmail,
            codeSecurity,
            signed: !!user,
            user,
            loading,
            dataUser
        }}
        >
         {children}   
        </AuthContext.Provider>
    )
}

export default AuthProvider;