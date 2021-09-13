const User = require('../models/userModel');
const jwt = require('jsonwebtoken');
const secret = 'secretpasswordkey';
const bcrypt = require('bcrypt')

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
               console.log(data.email)
               res.json(data.email)
            
           }
   
     },

    async delete(req, res) {
        const {_id} = req.params;
        const data = await User.findByIdAndDelete({_id});
        return res.json(data)
     },
    async update(req, res) {
        const {_id} = req.params;
        const {name, lastname, email, role, password} = req.body;
        const dataUser = {name, lastname, email, role, password}
        const data = await User.findOneAndUpdate({_id}, dataUser, {new: true});
        return res.json(data)
     },

    async create(req, res){
        const {name, lastname, email, role, password} = req.body;
        let data = {}

        let new_user =  await User.findOne({email});
        if(!new_user) {
            data = {name, lastname, email, role, password};
            new_user = await User.create(data);
           
            return res.status(200).json(new_user);
        } else {
            return res.status(500).json(new_user);
        }

    },

    async login(req, res) {
        const {email, password} = req.body;
        console.log(email, password);

        const user = await User.findOne({ email }).select('+password')
    
    if(!user) {
      return res.status(200).json({ error: 'User not found.' })
    }

    if(!await bcrypt.compare(password, user.password)) {
     // return res.status(200).json(null);
      return res.status(200).json({ error: 'Invalid password.' });
    }

    user.password = undefined;

    console.log(user);
    const payload = {email};
    const token = jwt.sign(payload, secret, {
        expiresIn: '24h'
    })
    res.cookie('token', token, {httpOnly: true})
    res.status(200).json({auth: true, token: token, name: user.name, lastname: user.lastname, role: user.role, email: user.email, _id: user._id})   

    },

    async token(req, res) {
        const token = req.body.token || req.query.token || req.cookies.token || req.headers['x-access-token'];
        if(!token){
            res.json({status: 401, msg: 'Não Autorizado'})
        } else {
            jwt.verify(token, secret, function(err, decoded) {
                if(err) {
                    res.json({status: 401, msg: 'Não Autorizado' })
                } else {
                    res.json({status:200})
                }
            })
        }
    }
}