package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.room.Room
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.ProductCart
import br.fetter.rulethemall.service.AppDatabase
import kotlinx.android.synthetic.main.home_list.container
import kotlinx.android.synthetic.main.product_card_cart.view.*
import java.text.NumberFormat
import java.util.*

class CartActivity : AppCompatActivity() {
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Carrinho"
        setContentView(R.layout.activity_cart)
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
            val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()
            val productCartList =  db.ProductService().getProducts()
            runOnUiThread {
                updateScreen(productCartList)
            }
        }.start()
    }

    private  fun updateScreen(productCartList: List<ProductCart>) {
        container.removeAllViews()
        for (product in productCartList) {
            val card = layoutInflater.inflate(R.layout.product_card_cart, container, false)
            card.txtCartProductName.text = "produto ${product.nomeProduto}"
            container.addView(card)
        }
    }

    private fun createNewProducts() {
        val newNote = ProductCart(nomeProduto = "sofa",precProduto = 200.89,descProduto = "um sofá legal",idCategoria = 2,qntCart = 4)
        Thread {
            saveProduct(newNote)
        }.start()
    }

    private fun saveProduct(newProduct: ProductCart) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()
        db.ProductService().save(newProduct)
        getProducts()
    }
}