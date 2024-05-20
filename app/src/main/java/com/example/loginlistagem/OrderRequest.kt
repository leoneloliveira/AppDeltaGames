package com.example.loginlistagem

data class OrderRequest(
    val userId: Int,
    val total: Double,
    val products: List<Produto>
)
