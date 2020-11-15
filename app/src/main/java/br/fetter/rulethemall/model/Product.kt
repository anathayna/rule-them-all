package br.fetter.rulethemall.model

data class Product (
    val precProduto: Float,
    val idProduto: Int,
    val descProduto: String,
    val ativoProduto: Boolean,
    val idCategoria: Int,
    val nomeProduto: String,
    val qtdMinEstoque: Int,
    val descontoPromocao: Float,
)