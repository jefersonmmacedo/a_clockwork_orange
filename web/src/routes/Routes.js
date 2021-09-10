import { Switch } from "react-router-dom";
import CodeSecurity from "../Pages/CodeSecurity/CodeSecurity";
import Dashboard from "../Pages/Dashboard/Dashboard/Dashboard";
import EditRegister from "../Pages/Dashboard/EditRegister/EditRegister";
import EditScheduling from "../Pages/Dashboard/EditScheduling/EditScheduling";
import Scheduling from "../Pages/Dashboard/Scheduling/Scheduling";
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
        <RouteWrapper exact path="/dashboard" component={Dashboard}/>
        <RouteWrapper exact path="/scheduling" component={Scheduling}/>
        <RouteWrapper exact path="/editscheduling" component={EditScheduling}/>
        <RouteWrapper exact path="/editregister" component={EditRegister}/>
    </Switch>
    )
}

export default Routes;


