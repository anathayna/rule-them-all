package br.fetter.rulethemall.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductCart(
    @PrimaryKey(autoGenerate = true)
    val idProduto: Int? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val imageData: ByteArray? = null,
    val productName: String,
    val productPrice: Double,
    val productDescription: String,
    val quantity: Int,
    val purchased: Boolean = false
)