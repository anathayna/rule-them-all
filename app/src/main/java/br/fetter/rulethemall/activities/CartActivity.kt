package br.fetter.rulethemall.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.Order
import br.fetter.rulethemall.service.room.DatabaseHelper
import br.fetter.rulethemall.service.retrofit.ServiceApiHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.home_list.container
import kotlinx.android.synthetic.main.product_card_cart.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class CartActivity : AppCompatActivity() {
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private var list_of_items = arrayOf("remover", "1 unidade", "2 unidades", "3 unidades", "4 unidades", "5 unidades")
    private var productCartList: List<Order>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Carrinho"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_cart)
        DatabaseHelper(this)
        ServiceApiHelper()
        btnBuy.setOnClickListener {
            buyProducts()
        }
        getProducts()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getProducts() {
        Thread {
            productCartList =  DatabaseHelper.getCart()
            runOnUiThread {
                productCartList?.let { products ->
                    updateScreen(products)
                }
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private  fun updateScreen(productCartList: List<Order>) {
        container.removeAllViews()
        val totalOrder = getTotalOrderPrice(productCartList)
        txtTotalOrder.text = "total do pedido:  $totalOrder"

        for (product in productCartList) {
            val card = layoutInflater.inflate(R.layout.product_card_cart, container, false)
            card.txtCartProductName.text = product.productName
            card.txtCategory.text = product.categoryName
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            card.spinner_product.adapter = aa
            card.spinner_product.setSelection(product.quantity)
            card.txtPrice.text = formatter.format(product.totalPrice)

            card.spinner_product.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (position == 0) {
                        Thread {
                            DatabaseHelper.deleteProductOfCart(product)
                            getProducts()
                        }.start()
                    } else if (position != product.quantity) {
                        product.quantity = position
                        val newPrice = getNewProductPrice(product, position)
                        product.totalPrice = newPrice
                        Thread {
                            DatabaseHelper.editCartProduct(product)
                            getProducts()
                        }.start()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            try {
                val id: Int = this.resources.getIdentifier(product.imageName, "drawable", this.packageName)
                card.imgCartProduct.setImageResource(id)
            } catch (ex: Exception) {
                card.imgCartProduct.setImageResource(R.drawable.placeholder_image)
            }
            container.addView(card)
        }
    }

    private fun getTotalOrderPrice(productCartList: List<Order>): String {
        var totalPrice = 0
        for (product in productCartList) {
            totalPrice += ((product.totalPrice * 100).toInt())
        }
        val totalPriceDouble = (totalPrice / 100).toDouble()
        return formatter.format(totalPriceDouble)
    }

    private fun getNewProductPrice(product: Order, quantity: Int): Double {
        return (((product.unitPrice * 100).toInt() * quantity) / 100).toDouble()
    }

    private fun buyProducts() {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        val user = getCurrentUser()
        user?.let {
            productCartList?.let { products ->
                for (product in products) {
                    product.buyDate = currentDate
                }
                ServiceApiHelper.buyProducts(products, it.uid) { error ->
                    if (error == null) {
                        removeProductsFromCart(products)
                    } else {
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun removeProductsFromCart(products: List<Order>) {
        Thread {
            DatabaseHelper.deleteProducts(products)
            runOnUiThread {
                Toast.makeText(this, "Compra realizada com sucesso", Toast.LENGTH_LONG)
                    .show()
                val i = Intent(this, HomeListActivity::class.java)
                startActivity(i)
                finish()
            }
        }.start()
    }

    private fun getCurrentUser(): FirebaseUser? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }
}