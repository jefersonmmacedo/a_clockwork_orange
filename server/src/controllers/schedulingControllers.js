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
        const {location, type, shift, date} = req.body;
        const infos = {location, type, shift, date}
        console.log(infos)

        const data = await Scheduling.find( {
                    location: new RegExp(`^${location}$`, 'i'), 
                    type: new RegExp(`^${type}$`, 'i'),
                    shift: {$in: [new RegExp(`^${shift}$`, 'i'), "Dia Inteiro"]},
                    date: new RegExp(`^${date}$`, 'i'), 
                   
          });
                      //  console.log(data);
                        console.log(data.length)
                        res.json({
                            result: data,
                            length: data.length
        })
     },
     async indexFilterUser(req, res) {
        const {_idUser, limit} = req.body;
        const infos = {_idUser}
        console.log(infos)

        const data = await Scheduling.find( {
                    _idUser: new RegExp(`^${_idUser}$`, 'i')       
          }).limit(limit);
             console.log(data.length)
              res.json({ result: data,
                         length: data.length
        })
     }
}


//