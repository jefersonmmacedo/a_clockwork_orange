const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const DataSchema = new mongoose.Schema({
    nome_usuario:String,
    email_usuário:String,
    tipo_usuario:{type:Number, default:1},
    senha_usuário:String
},{
    timestamps:true
}
)

DataSchema.pre('save',function(next){
    if(!this.isModified("senha_usuario")){
        return next();
    }
    this.senha_usuário = bcrypt.hashSync(this.senha_usuário,10);
    next();
})

const usuarios = mongoose.model('usarios',DataSchema);
module.exports = usuarios;