package com.nikolas.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.ListNameItemBinding
import com.nikolas.shoppinglist.databinding.NoteListItemBinding
import com.nikolas.shoppinglist.entities.NoteItem
import com.nikolas.shoppinglist.entities.ShoppingListName
import com.nikolas.shoppinglist.utils.HtmlManager

class ShopListNameAdapter() : ListAdapter<ShoppingListName, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ListNameItemBinding.bind(view)

        fun setData(shopListNameItem: ShoppingListName) = with(binding) {

            tvListName.text = shopListNameItem.name
            tvTime.text = shopListNameItem.time

            //itemView - это весь элемент
            itemView.setOnClickListener {

            }
            imDelete.setOnClickListener {

            }

        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_name_item, parent,false)
                return ItemHolder(inflater)
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingListName>() {

        override fun areItemsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}