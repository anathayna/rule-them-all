package br.fetter.rulethemall.service.room

import androidx.room.*
import br.fetter.rulethemall.model.Order

@Dao
interface ProductService {

    @Insert
    fun save(product: Order)

    @Insert
    fun save(product: List<Order>)

    @Query(value = "select * from `Order`")
    fun getAll(): List<Order>

    @Query(value = "select * from `Order` where onCart=:onCart")
    fun getCart(onCart: Boolean = true): List<Order>

    @Delete
    fun delete(product: Order)

    @Delete
    fun deleteAll(products: List<Order>)

    @Query("SELECT * FROM `Order` WHERE idProduto=:id")
    fun loadSingle(id: Int): Order

    @Query("SELECT * FROM `Order` WHERE productName LIKE '%' || :query || '%' ")
    fun filterByName(query: String): List<Order>

    @Update
    fun update(product: Order)

    @Update
    fun updateMany(products: List<Order>)
}