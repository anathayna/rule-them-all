package br.fetter.rulethemall.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.fetter.rulethemall.model.ProductCart
import retrofit2.Call
import retrofit2.http.GET

@Dao
interface ProductService {

    @Insert
    fun save(product: ProductCart)
}