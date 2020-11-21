package br.fetter.rulethemall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.fetter.rulethemall.R
import br.fetter.rulethemall.service.ProductService
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.home_list.*
import kotlinx.android.synthetic.main.home_list.container
import kotlinx.android.synthetic.main.product_card.view.*
import kotlinx.android.synthetic.main.product_card_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*

class CartActivity : AppCompatActivity() {
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Carrinho"
        setContentView(R.layout.activity_cart)
        updateScreen()
    }

    private  fun updateScreen() {
        container.removeAllViews()

        for (i in 1..10) {
            val card = layoutInflater.inflate(R.layout.product_card_cart, container, false)
            card.txtCartProductName.text = "produto $i"
            container.addView(card)
        }
    }
}