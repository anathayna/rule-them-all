package br.fetter.rulethemall.service.retrofit

import br.fetter.rulethemall.model.ListOrders
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ProductServiceApi {

    @GET("9a27c9e6c4be4983a453a1c3cd49e5a0/comprados2")
    fun list(): Call<List<ListOrders>>

    @Headers("Content-Type: application/json")
    @POST("9a27c9e6c4be4983a453a1c3cd49e5a0/comprados2")
    fun buyProducts(@Body products: ListOrders): Call<Void>

}