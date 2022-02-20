package com.nikolas.shoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nikolas.shoppinglist.entities.LibraryItem
import com.nikolas.shoppinglist.entities.NoteItem
import com.nikolas.shoppinglist.entities.ShoppingListItem
import com.nikolas.shoppinglist.entities.ShopListNameItem

@Database (entities = [LibraryItem::class, NoteItem::class,
    ShoppingListItem::class, ShopListNameItem::class], version = 1)
abstract class MainDataBase : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {

        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db"
                ).build()
                instance
            }
        }
    }
}