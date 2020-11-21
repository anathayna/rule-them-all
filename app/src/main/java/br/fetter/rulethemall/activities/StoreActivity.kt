package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import br.fetter.rulethemall.R
import br.fetter.rulethemall.service.AppDatabase
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val actionBar = supportActionBar
        actionBar!!.title = "Carrinho"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "AppDB").build()

        /*fab.setOnClickListener {
            //adicionar um spinner no alert para escolher a quantidade
            AlertDialog.Builder(this)
                .setTitle("Escolha a quantidade")
                .setPositiveButton("Comprar") { dialog, button ->
                    Toast.makeText(this@StoreActivity, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .create()
                .show()

        }*/
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
            val i = Intent(this, BuyActivity::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }
}