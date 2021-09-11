const express = require('express');
const percentageControllers = require('../controllers/percentageControllers');
const schedulingControllers = require('../controllers/schedulingControllers');
const securityCodeControllers = require('../controllers/securityCodeControllers');
const userControllers = require('../controllers/userControllers');
const routes = express.Router()

//CREATE USERS
routes.get('/api/user', userControllers.index) // Buscar usuarios
routes.post('/api/user/login', userControllers.login) //Logar
routes.get('/api/user/:_id', userControllers.indexOne)  //Buscar usuario pelo id ( ID passado na rota como parametro)
routes.get('/api/validator/:email', userControllers.indexFcamara) //Validar o email
routes.delete('/api/user/:_id', userControllers.delete) //Deletar usuario ( ID passado na rota como parametro)
routes.put('/api/user', userControllers.update) //Atualizar
routes.post('/api/user',userControllers.create) //Criar usuario

//CREATE PERCENTAGE
routes.get('/api/percentage', percentageControllers.index)
routes.delete('/api/percentage/:_id', percentageControllers.delete)
routes.put('/api/percentage', percentageControllers.update)
routes.post('/api/percentage',percentageControllers.create)

//SECURITY CODE
routes.get('/api/securitycode', securityCodeControllers.index)
routes.delete('/api/securitycode/:_id', securityCodeControllers.delete)
routes.put('/api/securitycode', securityCodeControllers.update)
routes.post('/api/securitycode',securityCodeControllers.create)

//SCHEDULING CREATE
routes.get('/api/scheduling', schedulingControllers.index)
routes.delete('/api/scheduling/:_id', schedulingControllers.delete)
routes.put('/api/scheduling', schedulingControllers.update)
routes.post('/api/scheduling',schedulingControllers.create)



module.exports = routes;