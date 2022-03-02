package com.nikolas.shoppinglist.db

import androidx.room.*
import androidx.room.Dao
import com.nikolas.shoppinglist.entities.LibraryItem
import com.nikolas.shoppinglist.entities.NoteItem
import com.nikolas.shoppinglist.entities.ShopListNameItem
import com.nikolas.shoppinglist.entities.ShopListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query ("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>

    @Query ("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>

    @Query ("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)

    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    @Insert
    suspend fun insertShopListName(nameItem: ShopListNameItem)

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query ("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Query ("DELETE FROM shop_list_item WHERE listId LIKE :listId")
    suspend fun deleteShopListItemsByListId(listId: Int)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateListItem(item: ShopListItem)

    @Update
    suspend fun updateShopListName(shopListNameItem: ShopListNameItem)
}