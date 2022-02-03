package com.nikolas.shoppinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.nikolas.shoppinglist.R

object FragmentManager {
    var currentFrag: BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity) {

        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.placeHolder, newFrag)
        transaction.commit()
        currentFrag = newFrag
    }
}