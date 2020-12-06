package br.fetter.rulethemall.service.room

import androidx.room.Database
import androidx.room.RoomDatabase
import br.fetter.rulethemall.model.Order

@Database(entities = arrayOf(Order::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ProductService(): ProductService
}