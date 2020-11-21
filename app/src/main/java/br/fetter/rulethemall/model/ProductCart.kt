package br.fetter.rulethemall.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductCart (
    @PrimaryKey(autoGenerate = true)
    val idProduto: Int,
    val nomeProduto: String,
    val precProduto: Float,
    val descProduto: String,
    val idCategoria: Int,
    val qntCart: Int,
)