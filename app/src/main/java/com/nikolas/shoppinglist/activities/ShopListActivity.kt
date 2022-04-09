package com.nikolas.shoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.ActivityShopListBinding
import com.nikolas.shoppinglist.db.MainViewModel
import com.nikolas.shoppinglist.db.ShopListItemAdapter
import com.nikolas.shoppinglist.dialogs.DeleteDialog
import com.nikolas.shoppinglist.dialogs.DeleteItemDialog
import com.nikolas.shoppinglist.dialogs.EditListItemDialog
import com.nikolas.shoppinglist.entities.LibraryItem
import com.nikolas.shoppinglist.entities.ShopListItem
import com.nikolas.shoppinglist.entities.ShopListNameItem
import com.nikolas.shoppinglist.utils.ShareHelper

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {

    private lateinit var binding: ActivityShopListBinding
    private lateinit var defPref: SharedPreferences
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        init()
        actionBarSettings()
        initRcView()
        listItemObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcher()
        return true
    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItems("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
        setTitle(shopListNameItem?.name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                saveItemCount()
                finish()
            }
            R.id.save_item -> {
                addNewShopItem(edItem?.text.toString())
            }
            R.id.delete_list -> {
                mainViewModel.deleteShopList(shopListNameItem?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteShopList(shopListNameItem?.id!!, false)
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(
                    ShareHelper.shareShopList(adapter?.currentList!!, shopListNameItem?.name!!),
                    "Поделиться с помощью"
                ))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem(name: String) {
        if (name.isEmpty()) {
            return
        }
        val item = ShopListItem(
            null,
            name,
            "",
            false,
            shopListNameItem?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShopItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).observe(this,{
            adapter?.submitList(it)
            binding.tvEmpty.visibility = if(it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this, {
            val tempShopList = ArrayList<ShopListItem>()
            it.forEach { item ->
                val shopItem = ShopListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)
            binding.tvEmpty.visibility = if(it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    private fun initRcView() = with(binding) {
        adapter = ShopListItemAdapter(this@ShopListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)

        val swapHelper = getSwapItem()
        swapHelper.attachToRecyclerView(binding.rcView)

        rcView.adapter = adapter
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shopListNameItem?.id!!).removeObservers(this@ShopListActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }

        }
    }

    private fun init() {
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    companion object {
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopListItem, state: Int) {
        when(state) {
            ShopListItemAdapter.CHECK_BOX -> {
                mainViewModel.updateListItem(shopListItem)
                saveItemCount()
            }
            ShopListItemAdapter.EDIT -> {
                editListItem(shopListItem)
            }
            ShopListItemAdapter.EDIT_LIBRARY_ITEM -> {
                editLibraryItem(shopListItem)
            }
            ShopListItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
            ShopListItemAdapter.ADD -> {
                addNewShopItem(shopListItem.name)
            }
        }

    }

    override fun onDeleteItem(shopListItem: ShopListItem, listId: Int) {
        mainViewModel.deleteItem(shopListItem.id!!, listId)
    }

    private fun editListItem(item: ShopListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }
        })
    }

    private fun editLibraryItem(item: ShopListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
        })
    }

    private fun saveItemCount() {
        var checkedItemCounter = 0
        adapter?.currentList?.forEach {
            if(it.itemChecked) {
                checkedItemCounter++
            }
        }

        val tempShopListNameItem = shopListNameItem?.copy(
            allItemCounter = adapter?.itemCount!!,
            checkedItemsCounter = checkedItemCounter
        )
        mainViewModel.updateListName(tempShopListNameItem!!)
    }

    private fun getSelectedTheme(): Int {
        return if(defPref.getString("theme_key", "blue") == "blue") {
            R.style.Theme_ShoppingListBlue
        } else {
            R.style.Theme_ShoppingListRed
        }
    }

    private fun getSwapItem(): ItemTouchHelper {
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                DeleteItemDialog.showDialog(this@ShopListActivity, object : DeleteItemDialog.Listener {
                    override fun onClick() {
                        adapter?.removeItem(viewHolder.adapterPosition, shopListNameItem?.id!!.toInt(),this@ShopListActivity)
                    }

                    override fun onCancel() {
                        binding.rcView.adapter?.notifyDataSetChanged()
                    }

                })

            }

        })
    }

    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()
    }
}