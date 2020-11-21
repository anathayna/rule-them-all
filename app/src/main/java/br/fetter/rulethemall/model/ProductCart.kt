package br.fetter.rulethemall.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductCart(
    @PrimaryKey(autoGenerate = true)
    val idProduto: Int? = null,
    val nomeProduto: String,
    val precProduto: Double,
    val descProduto: String,
    val idCategoria: Int,
    val qntCart: Int,
    val purchased: Boolean = false,
)