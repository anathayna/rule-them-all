package br.fetter.rulethemall.service

import androidx.room.Database
import androidx.room.RoomDatabase
import br.fetter.rulethemall.model.Product

@Database(entities = arrayOf(Product::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ProductService(): ProductService
}