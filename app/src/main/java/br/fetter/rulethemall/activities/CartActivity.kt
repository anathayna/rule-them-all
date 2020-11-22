package br.fetter.rulethemall.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.ProductCart
import br.fetter.rulethemall.service.DatabaseHelper
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.home_list.container
import kotlinx.android.synthetic.main.product_card_cart.view.*
import java.text.NumberFormat
import java.util.*


class CartActivity : AppCompatActivity() {
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    private var list_of_items = arrayOf("remover", "1 unidade", "2 unidades", "3 unidades", "4 unidades", "5 unidades")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Carrinho"
        setContentView(R.layout.activity_cart)
        DatabaseHelper(this)
        getProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addItem) {
            createNewProducts()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProducts() {
        Thread {
            val productCartList =  DatabaseHelper.getCartProducts()
            runOnUiThread {
                updateScreen(productCartList)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private  fun updateScreen(productCartList: List<ProductCart>) {
        container.removeAllViews()
        val totalOrder = getTotalOrderPrice(productCartList)
        txtTotalOrder.text = "total do pedido:  $totalOrder"

        for (product in productCartList) {
            val card = layoutInflater.inflate(R.layout.product_card_cart, container, false)
            card.txtCartProductName.text = product.productName
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

    private fun getTotalOrderPrice(productCartList: List<ProductCart>): String {
        var totalPrice = 0
        for (product in productCartList) {
            totalPrice += ((product.totalPrice * 100).toInt())
        }
        val totalPriceDouble = (totalPrice / 100).toDouble()
        return formatter.format(totalPriceDouble)
    }

    private fun getNewProductPrice(product: ProductCart, quantity: Int): Double {
        return (((product.unitPrice * 100).toInt() * quantity) / 100).toDouble()
    }

    private fun createNewProducts() {
        val newProduct = ProductCart(
            productName = "Tênis Vans old school",
            unitPrice = 200.00,
            totalPrice = 800.00,
            productDescription = "o melhor tenis que você pade querer está aqui",
            quantity = 4,
            imageName = "vans"
        )
        Thread {
            DatabaseHelper.addProductToCart(newProduct)
            getProducts()
        }.start()
    }
}