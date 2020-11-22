package br.fetter.rulethemall.activities

import android.content.Intent
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
import br.fetter.rulethemall.model.ProductCart
import br.fetter.rulethemall.service.AppDatabase
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val actionBar = supportActionBar
        actionBar!!.title = "Carrinho"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        /*val nameP = intent.getStringExtra("")
        val descP = intent.getStringExtra("")
        val precP = intent.getDoubleExtra()*/

        txtNameP.setText("Tenis VANS")
        descP.setText("Um tenis maneiro pra um role de skate, demoro")
        txtPriceP.setText("290,00")

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()

        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Escolha a quantidade")
                .setPositiveButton("Comprar") { dialog, button ->
                    //addProductCart()
                    Toast.makeText(this@StoreActivity, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .create()
                .show()

        }
    }

    fun updateProductCart() {

    }

    fun addProductCart(idProduto: Int?, nomeProduto: String, precProduto: Double, descProduto: String, qntCart: Int) {
        /*val db =
            Room.databaseBuilder(this, AppDatabase::class.java, "AppDb").build()
        db.ProductService()*/
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
}