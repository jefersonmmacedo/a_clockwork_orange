const Percentage = require('../models/percentageModel')

module.exports = {
    async index(req, res) {
       const data = await Percentage.find();
       res.json(data)
    },

    async delete(req, res) {
        const {_id} = req.params;
        const data = await Percentage.findByIdAndDelete({_id});
        return res.json(data)
     },
    async update(req, res) {
        const {_id, percentage} = req.body;
        const dataUser = {percentage}
        const data = await Percentage.findOneAndUpdate({_id}, dataUser, {new: true});
        return res.json(data)
     },

    async create(req, res){
        const {percentage} = req.body;
        let data = {}

        let new_percentage =  await Percentage.findOne({percentage});
        if(!new_percentage) {
            data = {percentage};
            new_percentage = await Percentage.create(data);
           
            return res.status(200).json(new_percentage);
        } else {
            return res.status(500).json(new_percentage);
        }

    }
}