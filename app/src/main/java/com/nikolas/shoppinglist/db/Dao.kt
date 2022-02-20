package com.nikolas.shoppinglist.db

import androidx.room.*
import androidx.room.Dao
import com.nikolas.shoppinglist.entities.NoteItem
import com.nikolas.shoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query ("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertShopListName(nameItem: ShopListNameItem)

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateShopListName(shopListNameItem: ShopListNameItem)
}