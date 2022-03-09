package com.nikolas.shoppinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.ActivityMainBinding
import com.nikolas.shoppinglist.dialogs.NewListDialog
import com.nikolas.shoppinglist.fragments.FragmentManager
import com.nikolas.shoppinglist.fragments.NoteFragment
import com.nikolas.shoppinglist.fragments.ShopListNamesFragment
import com.nikolas.shoppinglist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        binding.bNav.selectedItemId = R.id.shop_list
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.OnClickNew()
                }
            }
            true
        }
    }

    override fun onClick(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
}