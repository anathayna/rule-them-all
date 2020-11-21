package br.fetter.rulethemall.service

import androidx.room.Insert
import androidx.room.Query
import br.fetter.rulethemall.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {

    @GET("android/rest/produto")
    fun list(): Call<List<Product>>

    @Insert
    fun save(product: Product)

    @Query("delete from Product where id = :idProduto")
    fun delete(id: Int)
}