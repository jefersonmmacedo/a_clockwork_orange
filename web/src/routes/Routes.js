import { Switch } from "react-router-dom";
import CodeSecurity from "../Pages/CodeSecurity/CodeSecurity";
import Home from "../Pages/Home/Home";
import Login from "../Pages/Login/Login";
import Password from "../Pages/Password/Password";
import Register from "../Pages/Register/Register";
import RouteWrapper from "./Route";

function Routes() {
    return (

    <Switch>
        <RouteWrapper exact path="/" component={Home}/>
        <RouteWrapper exact path="/login" component={Login}/>
        <RouteWrapper exact path="/password" component={Password}/>
        <RouteWrapper exact path="/codesecurity" component={CodeSecurity}/>
        <RouteWrapper exact path="/password" component={Password}/>
        <RouteWrapper exact path="/register" component={Register}/>
    </Switch>
    )
}

export default Routes;


