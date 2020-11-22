package br.fetter.rulethemall.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.ProductCart
import br.fetter.rulethemall.service.DatabaseHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_list.container
import kotlinx.android.synthetic.main.product_card_cart.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.util.*

class CartActivity : AppCompatActivity() {
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))

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

    private  fun updateScreen(productCartList: List<ProductCart>) {
        container.removeAllViews()
        for (product in productCartList) {
            val card = layoutInflater.inflate(R.layout.product_card_cart, container, false)
            card.txtCartProductName.text = "produto ${product.productName}"
            //card.imgCartProduct.setImageBitmap(loadImageFromBytes(product.imageData))
            container.addView(card)
        }
    }

    private fun createNewProducts() {
//        // peguei imagem da net
//        val bitmapImage = Picasso.get()
//            .load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/1")
//            .error(R.drawable.image)
//            .get()
//        // converti para byteArray
//        val stream = ByteArrayOutputStream()
//        bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, stream)
//        val byteArrayImage = stream.toByteArray()

        val newProduct = ProductCart(productName = "sofa", productPrice = 200.89, productDescription = "um sofá legal", quantity = 4)
        Thread {
            DatabaseHelper.addProductToCart(newProduct)
            getProducts()
        }.start()
    }
}