const express = require('express');
const percentageControllers = require('../controllers/percentageControllers');
const schedulingControllers = require('../controllers/schedulingControllers');
const securityCodeControllers = require('../controllers/securityCodeControllers');
const userControllers = require('../controllers/userControllers');
const routes = express.Router()

//CREATE USERS
routes.get('/api/users', userControllers.index)
routes.get('/api/users/:_id', userControllers.indexOne)
routes.delete('/api/users/:_id', userControllers.delete)
routes.put('/api/users', userControllers.update)
routes.post('/api/users',userControllers.create)

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