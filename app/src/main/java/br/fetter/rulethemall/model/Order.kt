package br.fetter.rulethemall.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Order(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idProduto") val idProduto: Int? = null,
    @SerializedName("productName") val productName: String,
    @SerializedName("unitPrice") val unitPrice: Double,
    @SerializedName("totalPrice") var totalPrice: Double = 0.0,
    @SerializedName("productDescription") val productDescription: String,
    @SerializedName("quantity") var quantity: Int = 1,
    @SerializedName("onCart") var onCart: Boolean = false,
    @SerializedName("imageName") val imageName: String,
    @SerializedName("categoryName") val categoryName: String? = "",
    @SerializedName("buyDate") var buyDate: String = "dd/MM/yyyy"
)