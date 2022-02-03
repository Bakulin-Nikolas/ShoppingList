package com.nikolas.shoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity (tableName = "shopping_list_names")
data class ShoppingListNames(

    @PrimaryKey (autoGenerate = true)
    val id: Int?,

    @ColumnInfo (name = "name")
    val name: String,

    @ColumnInfo (name = "time")
    val time: String,

    @ColumnInfo (name = "allItemCount")
    val allItemCounter: Int,

    @ColumnInfo (name = "checkedItemsCounter")
    val checkedItemsCounter: Int,

    @ColumnInfo (name = "itemsIds")
    val itemsIds: String,

):Serializable
