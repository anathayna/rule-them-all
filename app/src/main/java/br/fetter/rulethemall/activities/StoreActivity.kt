package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.Order
import br.fetter.rulethemall.service.room.DatabaseHelper
import kotlinx.android.synthetic.main.activity_store.*
import java.text.NumberFormat
import java.util.*

class StoreActivity : AppCompatActivity() {

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        DatabaseHelper(this)
        loadActionbar()
        setupLayout(intent)
        setupFab()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_store, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.cartStore) {
            val i = Intent(this, CartActivity::class.java)
            startActivity(i)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupLayout(intent: Intent) {
        val idProduct = intent.getIntExtra("idProduct", 0)
        Thread {
            order = DatabaseHelper.getOrder(idProduct)
            runOnUiThread {
                order?.let { loadedOrder ->
                    txtNameP.text = loadedOrder.productName
                    descP.setText(loadedOrder.productDescription)
                    txtPriceP.text = formatter.format(loadedOrder.unitPrice)
                    try {
                        val id: Int = this.resources.getIdentifier(loadedOrder.imageName, "drawable", this.packageName)
                        imgP.setImageResource(id)
                    } catch (ex: Exception) {
                        imgP.setImageResource(R.drawable.placeholder_image)
                    }
                }
            }
        }.start()
    }

    private fun setupFab() {
        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.addToCart))
                .setPositiveButton(getString(R.string.yes)) { dialog, button ->
                    addProductToCart()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show()
        }
    }

    private fun addProductToCart() {
        order?.let { orderToAddonCart ->
            orderToAddonCart.onCart = true
            orderToAddonCart.totalPrice = orderToAddonCart.unitPrice
            Thread {
                DatabaseHelper.addProductToCart(orderToAddonCart)
                runOnUiThread {
                    val i = Intent(this, CartActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }.start()
        }
    }

    private fun loadActionbar() {
        supportActionBar?.title = getString(R.string.product)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}