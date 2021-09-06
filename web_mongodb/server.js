const express = require('express');
const cookieParser = require('cookie-parser')
const cors = require('cors');
const path = require('path');
const { json } = require('express');
const mongoose = require('mongoose');
const routes = require('./src/routes');
const app = express();
const port = process.env.PORT || 3000;

mongoose.connect('mongodb+srv://EduardoConrado:<password>@sqad34.rjest.mongodb.net/A_Clockwork_Orange',{
    useUnifiedTopology:true,
    useNewUrlParser:true,
}, function(err){
    if(err){
        console.log(err)
    }else{
        console.log("MongoDB Conectado com Sucesso!")
    }
})

app.use(cors())
app.use(cookieParser())
app.use(express.json())
app.use(routes);

app.listen(3000, function(){
    console.log(`Server runing on port ${port}`)
})