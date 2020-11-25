package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.inflate
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.ProductCart
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.home_list.*
import kotlinx.android.synthetic.main.product_card.view.*
import kotlinx.android.synthetic.main.product_card.view.txtPrice
import kotlinx.android.synthetic.main.product_card_cart.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap

class HomeListActivity : AppCompatActivity() {

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))
    var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Lista de produtos"
        setContentView(R.layout.home_list)

        if (getCurrentUser() == null) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_user_login_default)
                    .build(), 0
            )
        } else {
            configureDatabase()
            Toast.makeText(this, "yaaay autenticado", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            val response = IdpResponse.fromResultIntent(data)

            if(resultCode == RESULT_OK) {
                configureDatabase()
                Toast.makeText(this, "yaaay autenticado", Toast.LENGTH_LONG).show()
            } else {
                finishAffinity()
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    fun configureDatabase() {
        val user = getCurrentUser()

        user?.let {
            database = FirebaseDatabase.getInstance().reference.child("listas")

            val changeListener = object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("HomeListActivity", "listener", databaseError.toException())
                    Toast.makeText(this@HomeListActivity, "falha na rede", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    refreshUI(handleData(dataSnapshot))
                }
            }

            database?.addValueEventListener(changeListener)
        }
    }

    fun refreshUI(productList: List<ProductCart>) {
        container.removeAllViews()

        productList.forEach {
            val product = layoutInflater.inflate(R.layout.product_card, container, false)

            product.txtProductName.text = it.productName
            product.txtPrice.text = formatter.format(it.unitPrice)
            product.txtCategoria.text = it.categoryName

            try {
                val id: Int = this.resources.getIdentifier(it.imageName, "drawable", this.packageName)
                product.imgProduct.setImageResource(id)
            } catch (ex: Exception) {
                product.imgProduct.setImageResource(R.drawable.placeholder_image)
            }

            container.addView(product)
        }
    }

    fun handleData(dataSnapshot: DataSnapshot): List<ProductCart> {
        val product = arrayListOf<ProductCart>()

        dataSnapshot.child("produtos").children.forEach {
            val map = it.value as HashMap<String, Any>
            val nome = map.getValue("productName") as String
            val image = map.getValue("imageName") as String
            val desc = map.getValue("productDescription") as String
            val preco = map.getValue("unitPrice") as Double
            val category = map.getValue("categoryName") as String

            val item = ProductCart(
                productName = nome,
                unitPrice = preco,
                productDescription = desc,
                imageName = image,
                categoryName = category
            )

            product.add(item)
        }

        return product
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cart) {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.menuMyOrders) {
            val intent = Intent(this, LastOrdersActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}