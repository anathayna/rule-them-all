package br.fetter.rulethemall.service.retrofit

import br.fetter.rulethemall.model.ListOrders
import retrofit2.Call
import retrofit2.http.*

interface ProductServiceApi {

    @GET("{apiPath}/{userUID}")
    fun list(@Path("apiPath") apiPath: String, @Path("userUID") userUID: String): Call<List<ListOrders>>

    @Headers("Content-Type: application/json")
    @POST("{apiPath}/{userUID}")
    fun buyProducts(@Body products: ListOrders, @Path("apiPath") apiPath: String, @Path("userUID") userUID: String): Call<Void>

}