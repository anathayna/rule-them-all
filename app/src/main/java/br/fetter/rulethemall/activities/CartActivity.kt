package br.fetter.rulethemall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.fetter.rulethemall.R

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Carrinho"
        setContentView(R.layout.activity_cart)
    }
}