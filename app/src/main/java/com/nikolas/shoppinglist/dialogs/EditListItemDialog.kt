package com.nikolas.shoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.EditListItemDialogBinding
import com.nikolas.shoppinglist.databinding.NewListDialogBinding
import com.nikolas.shoppinglist.entities.ShopListItem

object EditListItemDialog {
    fun showDialog(context: Context, item: ShopListItem, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            bUpdate.setOnClickListener {
                if(edName.text.toString().isNotEmpty()) {
                    listener.onClick(item.copy(name = edName.text.toString(), itemInfo = edInfo.text.toString()))
                }
                dialog?.dismiss()
            }

        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()

    }

    interface Listener {
        fun onClick(item: ShopListItem)
    }
}