package br.fetter.rulethemall.service

import br.fetter.rulethemall.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {

    @GET("android/rest/produto")
    fun list(): Call<List<Product>>
}