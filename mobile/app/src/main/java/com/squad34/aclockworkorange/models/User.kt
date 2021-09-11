package com.squad34.aclockworkorange.models

import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.gson.jsonBody
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.mongodb.User
import org.bson.types.ObjectId
import retrofit2.converter.gson.GsonConverterFactory

data class UserComplete(
    val id: String,
    val user: String,
    val email: String,
    val role: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val v: Int,
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

}

fun getUser() {
    val (_, _, result) = Fuel.get("http://127.0.0.1:3001/api/user")
        .responseString()
    println(result)
}

fun login() {
    val email = "edu@fcamara.com.br"

    val (_, _, result) = Fuel.post("http://127.0.0.1:3001/api/user/login")
        .responseString()
    println(result)
}

fun getId() {
    val id = "613cfe6672fd2051842d6e9b"

    val (_, _, result) = ("http://127.0.0.1:3001/api/user/$id")
        .httpGet()
        .responseString()
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
    val user = UserComplete("613cfe6672fd2051842d6e9b", "Eduardo", "edu@fcamara.com.br", "Scrum Master", "","2021-09-11T19:07:18.849Z", "2021-09-11T19:07:18.849Z", 0)

    val id = "613d339a72fd2051842d6eb0"
    val (_, _, result) = ("http://127.0.0.1:3001/api/user/$id")
        .httpPut(listOf("user" to "Eduardo", "email" to "edu@fcamara.com.br", "role" to "Scrum Master", "password" to "56789"))
        .responseString()
    println(result)
}

fun createUser() {
    val user = UserComplete("613cfe6672fd2051842d6e9b", "Eduardo", "edu@fcamara.com.br", "Scrum Master", "12345","2021-09-11T19:07:18.849Z", "2021-09-11T19:07:18.849Z", 0)

    val (_, _, result) = "http://127.0.0.1:3001/api/user"
        .httpPost(listOf("user" to "Carlos Alberto", "email" to "carlosalberto@fcamara.com.br", "role" to "Scrum Master", "password" to "12345"))
        .responseString()
    println(result)
}

fun createScheduledDate() {
    val (_, _, result) = "http://127.0.0.1:3001/api/scheduling"
        .httpPost(listOf("location" to "São Paulo", "shift" to "Manhã", "type" to "Estação de Trabalho", "date" to "15/09/2021", "_idUser" to "613d339a72fd2051842d6eb0", "user" to "Carlos Alberto", "email" to "carlosalberto@fcamara.com.br", "role" to "Scrum Master"))
        .responseString()
    println(result)
}

fun getScheduling() {
    val (_, _, result) = "http://127.0.0.1:3001/api/scheduling"
        .httpGet()
        .responseString()
    println(result)
}

fun deleteScheduling() {
    val id = "613d238172fd2051842d6ea6"
    val (_, _, result) = "http://127.0.0.1:3001/api/scheduling/$id"
        .httpDelete()
        .responseString()
    println(result)
}

fun updateScheduledDate() {
    val (_, _, result) = "http://127.0.0.1:3001/api/scheduling"
        .httpPut(listOf("location" to "Santos", "shift" to "Manhã", "type" to "Estação de Trabalho", "date" to "17/09/2021", "_idUser" to "613d339a72fd2051842d6eb0", "user" to "Carlos Alberto", "email" to "carlosalberto@fcamara.com.br", "role" to "Scrum Master"))
        .responseString()
    println(result)
}






