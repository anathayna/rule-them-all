package br.fetter.rulethemall.service.retrofit

import br.fetter.rulethemall.model.ListOrders
import br.fetter.rulethemall.model.Order
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceApiHelper {

    companion object {

        private const val apiPath = "6746e9cb913943bda6a6ff72f5e75418"

        fun getProducts(userUid: String, myCallback: (result: List<ListOrders>?, error: String?) -> Unit) {
            val clienteHttp = OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://crudcrud.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clienteHttp)
                .build()
            val service = retrofit.create(ProductServiceApi::class.java)
            val call = service.list(apiPath, userUid)
            val callback = object: Callback<List<ListOrders>> {
                override fun onResponse(call: Call<List<ListOrders>>, response: Response<List<ListOrders>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { myCallback.invoke(it, null) }
                    } else {
                        myCallback.invoke(null, "Erro de response")
                    }
                }

                override fun onFailure(call: Call<List<ListOrders>>, t: Throwable) {
                    myCallback.invoke(null, "Erro de api")
                }

            }
            call.enqueue(callback)
        }

        fun buyProducts(products: List<Order>, userUid: String, myCallback: (error: String?) -> Unit) {
            val clienteHttp = OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://crudcrud.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clienteHttp)
                .build()
            val service = retrofit.create(ProductServiceApi::class.java)

            val call = service.buyProducts(ListOrders(products), apiPath, userUid)
            val callback = object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        myCallback.invoke(null)
                    } else {
                        myCallback.invoke("Erro de response")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    myCallback.invoke("Erro de api")
                }

            }
            call.enqueue(callback)
        }
    }
}