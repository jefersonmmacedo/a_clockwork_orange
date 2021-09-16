import { Switch } from "react-router-dom";
import CodeRecuperation from "../Pages/CodeRecuperation/CodeRecuperation";
import CodeSecurity from "../Pages/CodeSecurity/CodeSecurity";
import Account from "../Pages/Dashboard/Account/Account";
import Dashboard from "../Pages/Dashboard/Dashboard/Dashboard";
import EditRegister from "../Pages/Dashboard/EditRegister/EditRegister";
import EditScheduling from "../Pages/Dashboard/EditScheduling/EditScheduling";
import Scheduling from "../Pages/Dashboard/Scheduling/Scheduling";
import EmailRecuperation from "../Pages/EmailRecuperation/EmailRecuperation";
import Home from "../Pages/Home/Home";
import Login from "../Pages/Login/Login";
import Password from "../Pages/Password/Password";
import PasswordRecuperation from "../Pages/PasswordRecuperation/PasswordRecuperation";
import Register from "../Pages/Register/Register";
import RouteWrapper from "./Route";

function Routes() {
    return (

    <Switch>
        <RouteWrapper exact path="/" component={Home}/>
        <RouteWrapper exact path="/login" component={Login}/>
        <RouteWrapper exact path="/password" component={Password}/>
        <RouteWrapper exact path="/codesecurity" component={CodeSecurity}/>
        <RouteWrapper exact path="/register" component={Register}/>
        <RouteWrapper exact path="/email-recuperation" component={EmailRecuperation}/>
        <RouteWrapper exact path="/code-recuperation" component={CodeRecuperation}/>
        <RouteWrapper exact path="/password-recuperation" component={PasswordRecuperation}/>
        <RouteWrapper exact path="/dashboard/dashboard" component={Dashboard} isPrivate/>
        <RouteWrapper exact path="/dashboard/scheduling" component={Scheduling} isPrivate/>
        <RouteWrapper exact path="/dashboard/editscheduling" component={EditScheduling} isPrivate/>
        <RouteWrapper exact path="/dashboard/editregister" component={EditRegister} isPrivate/>
        <RouteWrapper exact path="/dashboard/account" component={Account} isPrivate/>
    </Switch>
    )
}

export default Routes;


