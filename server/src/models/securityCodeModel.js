const mongoose = require('mongoose');

const DataSchema = new mongoose.Schema({
    securityCode: String,
});


const SecurityCode = mongoose.model('securityCode', DataSchema);
module.exports = SecurityCode;