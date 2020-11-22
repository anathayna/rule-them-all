package br.fetter.rulethemall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.ProductCart
import br.fetter.rulethemall.service.DatabaseHelper
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.container
import kotlinx.android.synthetic.main.activity_last_orders.*
import kotlinx.android.synthetic.main.home_list.*
import kotlinx.android.synthetic.main.last_order_card.view.*
import kotlinx.android.synthetic.main.product_card_cart.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class LastOrdersActivity : AppCompatActivity() {

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private var productList: List<ProductCart>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Meus pedidos"
        setContentView(R.layout.activity_last_orders)
        DatabaseHelper(this)
        getProducts()
    }

    private fun getProducts() {
        Thread {
            productList =  DatabaseHelper.getPurcheasedProducts()
            runOnUiThread {
                productList?.let { products ->
                    updateScreen(products)
                }
            }
        }.start()
    }

    private  fun updateScreen(productCartList: List<ProductCart>) {
        lastOrdersContainer.removeAllViews()
        for (product in productCartList) {
            val card = layoutInflater.inflate(R.layout.last_order_card, container, false)
            card.txtProductName.text = product.productName
            card.txtTotalPrice.text = "total "+ formatter.format(product.totalPrice)
            card.txtBuyDate.text = "data da compra" + product.buyDate
            try {
                val id: Int = this.resources.getIdentifier(product.imageName, "drawable", this.packageName)
                card.imgOrder.setImageResource(id)
            } catch (ex: Exception) {
                card.imgOrder.setImageResource(R.drawable.placeholder_image)
            }
            lastOrdersContainer.addView(card)
        }
    }
}