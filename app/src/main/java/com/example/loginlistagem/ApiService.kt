package com.example.loginlistagem


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("/")
    fun getProdutos(): Call<List<Produto>>





}