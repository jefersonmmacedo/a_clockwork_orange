const User = require('../models/userModel');
const jwt = require('jsonwebtoken');
const secret = 'secretpasswordkey';

module.exports = {
    async index(req, res) {
       const data = await User.find();
       res.json(data)
    },

    async indexOne(req, res) {
        const {_id} = req.params;
        console.log({_id})
        const data = await User.findOne({_id});
        console.log(data)
        res.json(data)
     },


     async indexFcamara(req, res) {
        const {email} = req.params;
        console.log({email});

           const data = await User.findOne({email});

           if(!data) {  
               res.json(data)           
            } else {
               console.log(data)
               res.json(data)
            
           }
   
     },

    async delete(req, res) {
        const {_id} = req.params;
        const data = await User.findByIdAndDelete({_id});
        return res.json(data)
     },
    async update(req, res) {
        const {_id} = req.params;
        const {name, email, role, password} = req.body;
        const dataUser = {name, email, role, password}
        const data = await User.findOneAndUpdate({_id}, dataUser, {new: true});
        return res.json(data)
     },

    async create(req, res){
        const {name, email, role, password} = req.body;
        let data = {}

        let new_user =  await User.findOne({email});
        if(!new_user) {
            data = {name, email, role, password};
            new_user = await User.create(data);
           
            return res.status(200).json(new_user);
        } else {
            return res.status(500).json(new_user);
        }

    },

    async login(req, res){
        const {email, password} = req.body;
        console.log(email, password);
        

        User.find({email, password}, function(err, data) {
            console.log(data)
            if(err) {
                console.log(err);
                res.status(200).json({error: "Erro ao acessar o sistema. Tenta novamente mais tarde"})
            } else if(!data) {
                console.log(data)
                res.status(200).json({error: "Email ou senha não conferem"})
            } else {
                const payload = {email, password};
                const token = jwt.sign(payload, secret, {
                    expiresIn: '24h'
                })
                res.cookie('token', token, {httpOnly: true})
                res.status(200).json({auth: true, token: token, user: data.name, role: data.role, email: data.email, _id: data._id})
            }
        })
    }
}