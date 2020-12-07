package br.fetter.rulethemall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.Order
import br.fetter.rulethemall.service.retrofit.ServiceApiHelper
import kotlinx.android.synthetic.main.activity_cart.container
import kotlinx.android.synthetic.main.activity_last_orders.*
import kotlinx.android.synthetic.main.last_order_card.view.*
import java.text.NumberFormat
import java.util.*

class LastOrdersActivity : AppCompatActivity() {

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Meus pedidos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_last_orders)
        ServiceApiHelper()
    }

    override fun onResume() {
        super.onResume()
        getLastOrders()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getLastOrders() {
        ServiceApiHelper.getProducts { result, error ->
            result?.let { orders ->
                val products = arrayListOf<Order>()
                for (order in orders) {
                    products.addAll(order.getList())
                }
                updateScreen(products)
            }
        }
    }

    private  fun updateScreen(productCartList: List<Order>) {
        lastOrdersContainer.removeAllViews()
        for (product in productCartList) {
            val card = layoutInflater.inflate(R.layout.last_order_card, container, false)
            card.txtProductName.text = product.productName
            card.txtTotalPrice.text = "total "+ formatter.format(product.totalPrice)
            card.txtBuyDate.text = "data da compra " + product.buyDate
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