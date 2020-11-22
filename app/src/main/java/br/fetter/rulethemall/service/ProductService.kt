package br.fetter.rulethemall.service

import androidx.room.*
import br.fetter.rulethemall.model.ProductCart

@Dao
interface ProductService {

    @Insert
    fun save(product: ProductCart)

    @Query(value = "select * from ProductCart")
    fun getAll(): List<ProductCart>

    @Query(value = "select * from ProductCart where purchased=:purchased")
    fun getProducts(purchased: Boolean = false): List<ProductCart>

    @Delete
    fun delete(product: ProductCart)

    @Query("SELECT * FROM ProductCart WHERE idProduto=:id")
    fun loadSingle(id: Int): ProductCart

    @Update
    fun update(product: ProductCart)
}