const mongoose = require('mongoose');

const DataSchema = new mongoose.Schema({
    location: String,
    shift: String,
    createdAt: {type: Date, default: Date.now},
    date: Object,
    user: String,
    email: String,
    role: String,
});




const Scheduling = mongoose.model('scheduling', DataSchema);
module.exports = Scheduling;