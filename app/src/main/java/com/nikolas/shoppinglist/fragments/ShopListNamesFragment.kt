package com.nikolas.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.nikolas.shoppinglist.activities.MainApp
import com.nikolas.shoppinglist.databinding.FragmentShopListNamesBinding
import com.nikolas.shoppinglist.db.MainViewModel
import com.nikolas.shoppinglist.dialogs.NewListDialog
import com.nikolas.shoppinglist.entities.ShoppingListName
import com.nikolas.shoppinglist.utils.TimeManager


class ShopListNamesFragment : BaseFragment() {

    private lateinit var binding: FragmentShopListNamesBinding

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun OnClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {

    }


    private fun observer() {
        mainViewModel.allShopListNames.observe(viewLifecycleOwner, {

        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }
}