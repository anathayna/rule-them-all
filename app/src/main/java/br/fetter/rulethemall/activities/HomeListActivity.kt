package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.Product
import br.fetter.rulethemall.service.ProductService
import kotlinx.android.synthetic.main.home_list.*
import kotlinx.android.synthetic.main.product_card.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*

class HomeListActivity : AppCompatActivity() {

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Lista de produtos"
        setContentView(R.layout.home_list)
    }

    override fun onResume() {
        super.onResume()
        getProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cart) {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProducts() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ProductService::class.java)
        val call = service.list()

        val callback = object : Callback<List<Product>> {

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    updateScreen(response.body())
                } else {
                    Toast.makeText(this@HomeListActivity,
                        "Não foi possivel atualizar produtos",
                        Toast.LENGTH_LONG)
                        .show()
                    Log.e("ERRO_SERVICE", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@HomeListActivity,
                    "Erro de conexão",
                    Toast.LENGTH_LONG)
                    .show()
                Log.e("HomeListActivity", "getProducts",t)
            }
        }
        call.enqueue(callback)
    }

    private  fun updateScreen(productList: List<Product>?) {
        container.removeAllViews()
        productList?.forEach { product ->
            val card = layoutInflater.inflate(R.layout.product_card, container,false)
            card.txtProductName.text = product.nomeProduto
            card.txtPrice.text = formatter.format(product.precProduto)
            container.addView(card)
        }
    }
}