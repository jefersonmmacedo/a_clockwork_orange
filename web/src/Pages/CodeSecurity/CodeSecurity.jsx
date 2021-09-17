import './codeSecurity.css';
import React, { useContext, useState } from 'react';
import logoAside2 from '../../assets/images/logoAside2.svg';
import smartphoneAside from '../../assets/images/smartphoneAside.svg';
import Footer from '../../Components/Footer/Footer';
import ImageBody from '../../Components/ImageBody/ImageBody';
import { AuthContext } from '../../Contexts/Auth';



export default function CodeSecurity() {
  const [codeSecurity, setCodeSecurity] = useState('')
  const {validateCode} = useContext(AuthContext)

  function handleValidateCode(e) {
    e.preventDefault()

    //console.log(codeSecurity)
    if(codeSecurity !== '') {
      validateCode(codeSecurity)
    }
  }

  return (
    <div className="container">
      <div className="content">
        <div className="code-security">
          <div className="infos">
          <ImageBody image={smartphoneAside} alt='Image-SmartphoneUser'/>
            <div className="itens">
                <img src={logoAside2} alt="Logo" />
                <div className="text">
                  <p>Esse é o seu primeiro acesso ao sistema,
                    por favor informe seu código de acesso.
                  </p>
                </div>
                <span>Código de acesso</span>
                <input type="text" placeholder="Digite seu código de acesso" defaultValue={codeSecurity} onChange={(e) => setCodeSecurity(e.target.value)}/>
                <button className="button-primary" onClick={handleValidateCode}>Validar</button>
            </div>
          </div>
          </div>
          <Footer />
      </div>
    </div>
  );
};