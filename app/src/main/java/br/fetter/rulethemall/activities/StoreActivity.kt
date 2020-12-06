package br.fetter.rulethemall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.Order
import br.fetter.rulethemall.service.AppDatabase
import br.fetter.rulethemall.service.DatabaseHelper
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.product_card_cart.view.*

class StoreActivity : AppCompatActivity() {

    private var productImg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        loadActionbar()
        DatabaseHelper(this)
        setupLayout(intent)

        fab.setOnClickListener {
            val i = Intent(this, CartActivity::class.java)
            AlertDialog.Builder(this)
                .setTitle("Deseja adicionar o produto ao carrinho?")
                .setPositiveButton("Comprar") { dialog, button ->
                    selectedProduct()
                    startActivity(i)
                    Toast.makeText(this@StoreActivity, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .create()
                .show()

        }
    }

    fun setupLayout(intent: Intent) {
        txtNameP.setText(intent.getStringExtra("productName"))
        descP.setText(intent.getStringExtra("productDescription"))
        txtPriceP.setText(intent.getStringExtra("price"))
        productImg = intent.getStringExtra("imageName")
        try {
            val id: Int = this.resources.getIdentifier(productImg, "drawable", this.packageName)
            imgP.setImageResource(id)
        } catch (ex: Exception) {
            imgP.setImageResource(R.drawable.placeholder_image)
        }
    }

    fun selectedProduct() {
        val Product = Order(
            productName = "Tênis Vans old school",
            unitPrice = 200.00,
            totalPrice = 200.00,
            productDescription = "o melhor tenis que você pade querer está aqui",
            quantity = 1,
            imageName = "vans"
        )
        Thread {
            DatabaseHelper.addProductToCart(Product)
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.cart) {
            val i = Intent(this, CartActivity::class.java)
            startActivity(i)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadActionbar() {
        val actionBar = supportActionBar
        actionBar!!.title = "Comprar"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}