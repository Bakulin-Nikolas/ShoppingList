package com.nikolas.shoppinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.ActivityMainBinding
import com.nikolas.shoppinglist.fragments.FragmentManager
import com.nikolas.shoppinglist.fragments.NoteFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                }
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    Toast.makeText(this, "Shop list", Toast.LENGTH_SHORT).show()
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.OnClickNew()
                }
            }
            true
        }
    }
}