package com.nikolas.shoppinglist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nikolas.shoppinglist.entities.NoteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)
}