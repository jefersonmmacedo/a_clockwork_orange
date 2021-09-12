const mongoose = require('mongoose');

const DataSchema = new mongoose.Schema({
    location: String,
    shift: String,
    type: String,
    createdAt: {type: Date, default: Date.now},
    date: String,
    day: String,
    _idUser: String,
    name: String,
    lastname: String,    
    email: String,
    role: String,
});




const Scheduling = mongoose.model('scheduling', DataSchema);
module.exports = Scheduling;