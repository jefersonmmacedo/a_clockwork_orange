const User = require('../models/userModel')

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
        const {email} = req.body;
        console.log({email});

        if(email.includes('@fcamara.com.br')) {
           const data = await User.findOne({email});
           if(data === null) {
            res.json('Seja bem vindo. Este é seu primeiro acesso');           
           } else {
            console.log(data)
            res.json(data)
           }
        } else {
            res.json('Acesso negado, utilize seu email corporativo');
        }
     },
    async delete(req, res) {
        const {_id} = req.params;
        const data = await User.findByIdAndDelete({_id});
        return res.json(data)
     },
    async update(req, res) {
        const {_id, user, email, role, password} = req.body;
        const dataUser = {user, email, role, password}
        const data = await User.findOneAndUpdate({_id}, dataUser, {new: true});
        return res.json(data)
     },

    async create(req, res){
        const {user, email, role, password} = req.body;
        let data = {}

        let new_user =  await User.findOne({email});
        if(!new_user) {
            data = {user, email, role, password};
            new_user = await User.create(data);
           
            return res.status(200).json(new_user);
        } else {
            return res.status(500).json(new_user);
        }

    }

    // async login(req, res){

    // }
}