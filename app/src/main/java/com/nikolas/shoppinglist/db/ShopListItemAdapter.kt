package com.nikolas.shoppinglist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.ListNameItemBinding
import com.nikolas.shoppinglist.databinding.ShopLibraryListItemBinding
import com.nikolas.shoppinglist.databinding.ShopListItemBinding
import com.nikolas.shoppinglist.entities.ShopListNameItem
import com.nikolas.shoppinglist.entities.ShopListItem

class ShopListItemAdapter(private val listener: Listener) : ListAdapter<ShopListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0) {
            ItemHolder.createShopItem(parent)
        } else {
            ItemHolder.createLibraryItem(parent)
        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) {
            holder.setItemData(getItem(position), listener)
        } else {
            holder.setLibraryData(getItem(position), listener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setItemData(shopListItem: ShopListItem, listener: Listener) {
            val binding = ShopListItemBinding.bind(view)
            binding.apply {
                tvName.text = shopListItem.name
                tvInfo.text = shopListItem.itemInfo
                tvInfo.visibility = infoVisibility(shopListItem)
                chBox.isChecked = shopListItem.itemChecked
                setPaintFlagAndColor(binding)
                chBox.setOnClickListener {
                    setPaintFlagAndColor(binding)
                    listener.onClickItem(shopListItem.copy(itemChecked = chBox.isChecked))
                }
            }
        }
        fun setLibraryData(shopListItem: ShopListItem, listener: Listener) {

        }

        private fun setPaintFlagAndColor(binding: ShopListItemBinding) {
            binding.apply {
                if(chBox.isChecked) {
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_light))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_light))
                } else {
                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                }
            }
        }

        private fun infoVisibility(shopListItem: ShopListItem): Int {
            return if(shopListItem.itemInfo.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        companion object {
            fun createShopItem(parent: ViewGroup): ItemHolder {
                val inflater = LayoutInflater.from(parent.context).inflate(R.layout.shop_list_item, parent,false)
                return ItemHolder(inflater)
            }
            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                val inflater = LayoutInflater.from(parent.context).inflate(R.layout.shop_library_list_item, parent,false)
                return ItemHolder(inflater)
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopListItem>() {

        override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun onClickItem(shopListItem: ShopListItem)
    }

}