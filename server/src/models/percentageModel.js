const mongoose = require('mongoose');

const DataSchema = new mongoose.Schema({
    percentage: Number,
});


const Percentage = mongoose.model('percentage', DataSchema);
module.exports = Percentage;