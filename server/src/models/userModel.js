const mongoose = require('mongoose');
const brypt = require('bcrypt');

const DataSchema = new mongoose.Schema({
    name: String,
    lastname: String,
    email: String,
    role: String,
    password: String
}, {
    timestamps: true
});

DataSchema.pre('save', function (next) {
    if(!this.isModified("password")){
        return next()
    } 

    this.password = brypt.hashSync(this.password, 10);
    next()
});

DataSchema.pre('findOneAndUpdate', function (next) {
    let newPassword = this.getUpdate().password+'';
    if(newPassword.length < 55){
        this.getUpdate().password = brypt.hashSync(newPassword, 10);
    } 
    return next();
});


const User = mongoose.model('user', DataSchema);
module.exports = User;