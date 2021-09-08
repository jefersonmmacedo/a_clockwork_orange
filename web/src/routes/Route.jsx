import { useState } from 'react';
import {Route, Redirect} from 'react-router-dom';

function RouteWrapper({
    component: Component,
    isPrivate,
    ...rest
}){
    const [signed, setSigned] = useState(false);
    const [loading, setLoading] = useState(false);

    if(loading) {
        return(
            <div><h1>Carregando a página</h1></div>
        )
    }

    if(!signed && isPrivate) {
        return <Redirect to="/" />
    }

    return (
        <Route
        {...rest}
        render={props => (
            <Component {...props} />
        )}
        />
    )
}

export default RouteWrapper;