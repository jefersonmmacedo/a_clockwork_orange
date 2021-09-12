import { BrowserRouter } from 'react-router-dom';
import './Global.css';
import Routes from './routes/Routes';
import 'react-toastify/dist/ReactToastify.css'
import {ToastContainer} from 'react-toastify';
import AuthProvider from './Contexts/Auth';

function App() {
  return (
    <div className="App">
        <BrowserRouter>
         <AuthProvider>
          <ToastContainer autoClose={3000} theme="colored"/>
          <Routes />
         </ AuthProvider>
        </BrowserRouter>
    </div>
  );
}

export default App;
