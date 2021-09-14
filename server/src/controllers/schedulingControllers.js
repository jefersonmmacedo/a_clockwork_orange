const Scheduling = require('../models/schedulingModel')

module.exports = {
    async index(req, res) {
       const data = await Scheduling.find();
       res.json(data)
    },

    async indexOne(req, res) {
        const {_id} = req.params;
        console.log({_id})
        const data = await Scheduling.findOne({_id});
        console.log(data)
        res.json(data)
     },
    async delete(req, res) {
        const {_id} = req.params;
        const data = await Scheduling.findByIdAndDelete({_id});
        return res.json(data)
     },
    async update(req, res) {
        const {_id} = req.params;
        const {location, shift, type, date, day, _idUser, name,lastname, email, role} = req.body;
        const dataUser = {location, shift, type, date, day, _idUser, name,lastname, email, role}
        const data = await Scheduling.findOneAndUpdate({_id}, dataUser, {new: true});
        return res.json(data)
     },

    async create(req, res){
        const {location, shift, type, date, day, _idUser, name,lastname, email, role} = req.body;
        const data = {location, shift, type, date, day, _idUser, name,lastname, email, role};
        const  new_scheduling = await Scheduling.create(data);
           
        return res.json(new_scheduling);

    },
    async indexFilter(req, res) {
        const {location, type, shifit, date} = req.body;
        //const filter = {location, type}
        const data = await Scheduling.find( {
          $or: [
                    {'location': new RegExp(`^${location}$`, 'i')}, 
                    {'type': new RegExp(`^${type}$`, 'i')},
                    {'shifit': new RegExp(`^${shifit}$`, 'i')}, 
                    {'date': new RegExp(`^${date}$`, 'i')}
          ]
          });
       
        res.json(data)
     }
}


//