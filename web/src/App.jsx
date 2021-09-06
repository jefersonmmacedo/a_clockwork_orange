// Assim que importamos o css
import './Global.css';
import Home from './Pages/Home/Home';



// Criamos uma função, dentro dela que colocamos tudo que vamos renderizar em tela.
// Chamamos de componente funcional
function App() {
  return (
    <div className="App">
      
        <Home />
        
    </div>
  );
}

export default App;

// Dentro a function damos um return()
// Dentro das () colocamos uma div principal
// dentro dessa div, vem o HTML
// Note da div, que temos o atributo className. no html comum colocamos a class CSS, aqui colocamos como className