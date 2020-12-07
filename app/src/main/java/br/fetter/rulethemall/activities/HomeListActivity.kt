package br.fetter.rulethemall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.fetter.rulethemall.R
import br.fetter.rulethemall.model.Order
import br.fetter.rulethemall.service.room.DatabaseHelper
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.product_card.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.home_list.*
import kotlinx.android.synthetic.main.product_card.view.*
import kotlinx.android.synthetic.main.product_card.view.txtPrice
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class HomeListActivity : AppCompatActivity() {

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","BR"))
    var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Lista de produtos"
        setContentView(R.layout.home_list)
        DatabaseHelper(this)
        configureDataBase()
    }

    override fun onResume() {
        super.onResume()
        getProductsFormRoom()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                configureDatabase()
            } else {
                finishAffinity()
            }
        }
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

    private fun configureDataBase() {
        Thread {
            val values = DatabaseHelper.getHomeList()
            DatabaseHelper.deleteProducts(values)
            runOnUiThread {
                verifyUser()
            }
        }.start()
    }

    private fun verifyUser() {
        if (getCurrentUser() == null) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_user_login_default)
                    .build(), 0
            )
        } else {
            configureDatabase()
        }
    }

    private fun getCurrentUser(): FirebaseUser? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    private fun configureDatabase() {
        val user = getCurrentUser()

        user?.let {
            database = FirebaseDatabase.getInstance().reference.child("listas")

            val changeListener = object: ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE

                    scrollView.visibility = View.GONE
                    shimmer.visibility = View.VISIBLE
                    shimmer.startShimmer()

                    Log.w("HomeListActivity", "listener", databaseError.toException())
                    Toast.makeText(this@HomeListActivity, "falha na rede", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    saveProductsLocaly(handleData(dataSnapshot))
                }
            }

            database?.addValueEventListener(changeListener)

            scrollView.visibility = View.GONE
            shimmer.visibility = View.VISIBLE
            shimmer.startShimmer()
        }
    }

    private fun saveProductsLocaly(productList: List<Order>) {
        Thread {
            DatabaseHelper.addProducts(productList)
            runOnUiThread {
                getProductsFormRoom()
            }
        }.start()
    }

    private fun getProductsFormRoom() {
        Thread {
            val products = DatabaseHelper.getHomeList()
            runOnUiThread {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                refreshUI(products)
            }
        }.start()
    }

    fun refreshUI(productList: List<Order>) {
        container.removeAllViews()

        val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(800)
            .setBaseAlpha(0.9f)
            .setHighlightAlpha(0.7f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

        val drawShimmer = ShimmerDrawable()
        drawShimmer.setShimmer(shimmer)

        productList.forEach { order ->
            val productCard = layoutInflater.inflate(R.layout.product_card, container, false)

            productCard.txtProductName.text = order.productName
            productCard.txtPrice.text = formatter.format(order.unitPrice)
            productCard.txtCategoria.text = order.categoryName

            try {
                val id: Int = this.resources.getIdentifier(order.imageName, "drawable", this.packageName)
                productCard.imgProduct.setImageResource(id)
            } catch (ex: Exception) {
                productCard.imgProduct.setImageResource(R.drawable.placeholder_image)
            }

            productCard.setOnClickListener {
                if (!order.onCart) {
                    order.idProduto?.let { idOrder ->
                        val intent = Intent(this, StoreActivity::class.java)
                        intent.putExtra("idProduct", idOrder)
                        startActivity(intent)
                    }
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Esta produto já se encontra no carrinho")
                        .setNegativeButton("ir para o carrinho") { _, _ ->
                            val intent = Intent(this, CartActivity::class.java)
                            startActivity(intent)
                        }
                        .setPositiveButton("ok", null)
                        .create()
                        .show()
                }
            }
            container.addView(productCard)
        }
    }

    fun handleData(dataSnapshot: DataSnapshot): List<Order> {
        val product = arrayListOf<Order>()

        dataSnapshot.child("produtos").children.forEach {
            val map = it.value as HashMap<String, Any>
            val nome = map.getValue("productName") as String
            val image = map.getValue("imageName") as String
            val desc = map.getValue("productDescription") as String
            val preco = map.getValue("unitPrice") as Double
            val category = map.getValue("categoryName") as String

            val item = Order(
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
}