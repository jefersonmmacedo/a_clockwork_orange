const SecurityCode = require('../models/securityCodeModel')

module.exports = {
    async index(req, res) {
       const data = await SecurityCode.find();
       res.json(data)
    },

    async delete(req, res) {
        const {_id} = req.params;
        const data = await SecurityCode.findByIdAndDelete({_id});
        return res.json(data)
     },
    async update(req, res) {
        const {_id, securityCode} = req.body;
        const dataUser = {securityCode}
        const data = await SecurityCode.findOneAndUpdate({_id}, dataUser, {new: true});
        return res.json(data)
     },

    async create(req, res){
        const {securityCode} = req.body;
        let data = {}

        let new_securityCode =  await SecurityCode.findOne({securityCode});
        if(!new_securityCode) {
            data = {securityCode};
            new_securityCode = await SecurityCode.create(data);
           
            return res.status(200).json(new_securityCode);
        } else {
            return res.status(500).json(new_securityCode);
        }

    }
}