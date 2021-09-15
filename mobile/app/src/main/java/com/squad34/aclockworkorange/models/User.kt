package com.squad34.aclockworkorange.models


import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.fuel.gson.jsonBody
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory


data class User(
    val _id: String,
    val email: String,
    val role: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val v: Int,
    val name: String,
    val lastName: String
)



fun main() {



    //Funcionando
    //getUser()

    //Funciona mas a api aceita qualquer parametro
    //login()

    //Funcionando
    //getId()

    //Funcionando
    //deleteId()

    //Funciona
    //createUser()

    //Funciona
    //createScheduledDate()

    //Funciona
    //getScheduling()

    //Não funciona
    //updateUser()

    //Funcionando
    //deleteScheduling()

    //Não funciona
    //updateScheduledDate()

    //validator()

    //getTotalDate()

    //filterScheduling()
    getTotalDate()
}

fun getUser() {
    val (_, _, result) = Fuel.get("http://127.0.0.1:3001/api/user")
        .responseString()
    println(result)
}

fun validator() {
    val email = "eduardowasem@fcamara.com.br"
    val (_, _, result) = "http://127.0.0.1:3001/api/validator/$email"
        .httpGet()
        .responseString()
    println(result)

}

fun login() {
    val email = "eduardowasem@fcamara.com.br"
    val password = "12345"

    val (_, _, result) = ("http://127.0.0.1:3001/api/user/login/:email=$email&password=$password")
        .httpPost()
        .responseString()
    println(result)

   // "http://127.0.0.1:3001/api/user/login/?email=eduardowasem@fcamara.com.br&password=12345"
}

fun getId() {
    val id = "613cfe6672fd2051842d6e9b"

    val (_, _, result) = ("http://127.0.0.1:3001/api/user/$id")
        .httpGet()
        .response()




    println(result)
}

fun deleteId() {
    val id = "613cfe6672fd2051842d6e9b"

    val (_, _, result) = ("http://127.0.0.1:3001/api/user/$id")
        .httpDelete()
        .responseString()
    println(result)
}

fun updateUser() {
    //val user = UserComplete("613cfe6672fd2051842d6e9b", "Eduardo", "edu@fcamara.com.br", "Scrum Master", "","2021-09-11T19:07:18.849Z", "2021-09-11T19:07:18.849Z", 0)

    val id = "613d339a72fd2051842d6eb0"
    val (_, _, result) = ("http://127.0.0.1:3001/api/user/$id")
        .httpPut(listOf("user" to "Eduardo", "email" to "edu@fcamara.com.br", "role" to "Scrum Master", "password" to "56789"))
        .responseString()
    println(result)
}

fun createUser() {
    //val user = UserComplete("613cfe6672fd2051842d6e9b", "Eduardo", "edu@fcamara.com.br", "Scrum Master", "12345","2021-09-11T19:07:18.849Z", "2021-09-11T19:07:18.849Z", 0)

    val (_, _, result) = "http://127.0.0.1:3001/api/user"
        .httpPost(listOf("name" to "Marcos", "email" to "marcos@fcamara.com.br", "role" to "Scrum Master", "password" to "12345"))
        .responseString()
    println(result)
}

fun getTotalDate() {
    val id = "613df39d0248015f4766f9e8"
    val (_, _, result) = "http://127.0.0.1:3001/api/filter"
        .httpPost(listOf("location" to "São Paulo", "type" to "Estação de Trabalho", "shift" to "Tarde", "date" to "15/09/2021"))
        .responseString()
    println(result)
}

/*fun getScheduling() {
    val (_, _, result) = "http://127.0.0.1:3001/api/scheduling"
        .httpGet()
        .responseString()
    println(result)
}*/

fun filterScheduling() {
    val id = "61378b8bccae0e506c880f5c"
    val (_, _, result) = "http://127.0.0.1:3001/api/filter_user/$id"
        .httpPost()
        .responseString()
    println(result)
}

fun updateScheduledDate() {
    val (_, _, result) = "http://127.0.0.1:3001/api/filter_user"
        .httpPut(listOf("location" to "Santos", "shift" to "Manhã", "type" to "Estação de Trabalho", "date" to "17/09/2021", "_idUser" to "613d339a72fd2051842d6eb0", "user" to "Carlos Alberto", "email" to "carlosalberto@fcamara.com.br", "role" to "Scrum Master"))
        .responseString()
    println(result)
}






