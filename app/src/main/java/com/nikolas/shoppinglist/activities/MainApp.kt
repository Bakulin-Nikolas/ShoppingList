package com.nikolas.shoppinglist.activities

import android.app.Application
import com.nikolas.shoppinglist.db.MainDataBase


class MainApp : Application(){

    val database by lazy { MainDataBase.getDataBase(this) }
}