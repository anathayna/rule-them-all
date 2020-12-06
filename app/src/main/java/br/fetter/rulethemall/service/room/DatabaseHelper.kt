package br.fetter.rulethemall.service.room

import android.content.Context
import androidx.room.Room
import br.fetter.rulethemall.model.Order
import java.lang.ref.WeakReference

class DatabaseHelper(context: Context) {

    init {
        Companion.context = WeakReference(context)
    }

    companion object {

        private var context: WeakReference<Context>? = null
        private const val DATABASE_NAME: String = "AppDb"
        private var singleton: AppDatabase? = null

        private fun createDatabase(): AppDatabase {
            return Room.databaseBuilder(
                context?.get()
                    ?: throw IllegalStateException("initialize by calling constructor before calling DatabaseHelper.instance"),
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .build()
        }


        private val instance: AppDatabase
            @Synchronized get() {
                if (null == singleton)
                    singleton = createDatabase()

                return singleton as AppDatabase
            }

        fun getCartProducts(): List<Order> {
            val dao = instance.ProductService()
            return dao.getProducts(purchased = false)
        }

        fun editCartProduct(product: Order) {
            val dao = instance.ProductService()
            dao.update(product)
        }

        fun addProductToCart(product: Order) {
            val dao = instance.ProductService()
            dao.save(product)
        }

        fun deleteProductOfCart(product: Order) {
            val dao = instance.ProductService()
            dao.delete(product)
        }

        fun deleteProducts(products: List<Order>) {
            val dao = instance.ProductService()
            dao.deleteAll(products)
        }
    }
}