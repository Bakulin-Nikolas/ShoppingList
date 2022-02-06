package com.nikolas.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.NoteListItemBinding
import com.nikolas.shoppinglist.entities.NoteItem

class NoteAdapter(private val listener: Listener) : ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = NoteListItemBinding.bind(view)

        fun setData(note: NoteItem, listener: Listener) = with(binding) {

            tvTitle.text = note.title
            tvDescription.text = note.content
            tvTime.text = note.time
            //itemView - это весь элемент
            itemView.setOnClickListener {
                listener.onClickItem(note)
            }
            imDelete.setOnClickListener {
                //id не будет равен null
                listener.deleteItem(note.id!!)
            }

        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                val inflater = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent,false)
                return ItemHolder(inflater)
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {

        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }

}