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
    var totalPrice: Double = 0.0,
    val productDescription: String,
    var quantity: Int = 1,
    var purchased: Boolean = false,
    val imageName: String,
    var buyDate: String = "dd/MM/yyyy"
)