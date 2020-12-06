package br.fetter.rulethemall.service.room

import androidx.room.*
import br.fetter.rulethemall.model.Order

@Dao
interface ProductService {

    @Insert
    fun save(product: Order)

    @Query(value = "select * from `Order`")
    fun getAll(): List<Order>

    @Query(value = "select * from `Order` where purchased=:purchased")
    fun getProducts(purchased: Boolean = false): List<Order>

    @Delete
    fun delete(product: Order)

    @Delete
    fun deleteAll(products: List<Order>)

    @Query("SELECT * FROM `Order` WHERE idProduto=:id")
    fun loadSingle(id: Int): Order

    @Update
    fun update(product: Order)

    @Update
    fun updateMany(products: List<Order>)
}