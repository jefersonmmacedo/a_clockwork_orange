const port = 3001;
const express = require('express');
const mongoose = require('mongoose')
const bodyParser = require ('body-parser')
const cookieParser = require('cookie-parser');
const cors = require('cors');
const path = require('path');
const server = express();
const routes = require('./routes/routes');


mongoose.connect('mongodb://localhost:27017/cadUser', {
   useNewUrlParser: true, 
   useUnifiedTopology: true
}, (err) => {
    if(err) {
        console.log(err)
    } else {
        console.log('MongoDB Conectado com sucesso!')
    }
} )

server.use(cors());
server.use(cookieParser());
server.use(bodyParser.urlencoded({extends: true}))
server.use(express.json());

server.use(routes)

server.listen(port, (req, res) => {
    console.log('servidor inicado com sucesso. Acesse o link: http://localhost:' + port)
})