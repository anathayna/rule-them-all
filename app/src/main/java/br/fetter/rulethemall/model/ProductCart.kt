package br.fetter.rulethemall.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductCart(
    @PrimaryKey(autoGenerate = true)
    val idProduto: Int? = null,
    val productName: String,
    val unitPrice: Double,
    var totalPrice: Double,
    val productDescription: String,
    var quantity: Int,
    var purchased: Boolean = false,
    val imageName: String
)