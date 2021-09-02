const express = require ('express');
const bodyParser = require('body-parser');
const port = 3001

const server = express();

server.use(bodyParser.urlencoded({extends: true}));
server.use(bodyParser.json());

server.get("/", (req, res) => {
    res.send('A Clockwork Orange')
});

server.listen(port, () => {
    console.log(`Server ativo, acesse: http://localhost:${port}`)
})