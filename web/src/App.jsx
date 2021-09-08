import { BrowserRouter } from 'react-router-dom';
import './Global.css';
import Routes from './routes/Routes';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <Routes />
      </BrowserRouter>
        
    </div>
  );
}

export default App;
