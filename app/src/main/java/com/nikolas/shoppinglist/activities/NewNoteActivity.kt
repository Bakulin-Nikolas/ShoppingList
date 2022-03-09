package com.nikolas.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.databinding.ActivityNewNoteBinding
import com.nikolas.shoppinglist.entities.NoteItem
import com.nikolas.shoppinglist.fragments.NoteFragment
import com.nikolas.shoppinglist.utils.HtmlManager
import com.nikolas.shoppinglist.utils.MyTouchListener
import com.nikolas.shoppinglist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        setTextSize()
        onClickColorPicker()
//        actionMenuCallback()
    }

    private fun onClickColorPicker() = with(binding) {
        imRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red)
        }
        imBlack.setOnClickListener {
            setColorForSelectedText(R.color.picker_black)
        }
        imBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue)
        }
        imYellow.setOnClickListener {
            setColorForSelectedText(R.color.picker_yellow)
        }
        imGreen.setOnClickListener {
            setColorForSelectedText(R.color.picker_green)
        }
        imOrange.setOnClickListener {
            setColorForSelectedText(R.color.picker_orange)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = sNote as NoteItem
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
        edTitle.setText(note?.title)
        edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.id_save -> {
                setMainResult()
            }
            R.id.id_bold -> {
                setBoldForSelectedText()
            }
            R.id.id_color -> {
                if(binding.colorPicker.isShown) {
                    closeColorPicker()
                } else {
                    openColorPicker()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if(styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if(styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        }

        edDescription.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setMainResult()  {
        var editState = "new"
        val tempNote: NoteItem?
        if (note == null) {
            tempNote = createNewNote()
        } else {
            editState = "update"
            tempNote = updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun updateNote() : NoteItem? = with(binding) {
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text)
        )
    }

    private fun createNewNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text),
            TimeManager.getCurrentTime(),
            ""
        )
    }


    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val closeAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        closeAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        binding.colorPicker.startAnimation(closeAnim)
    }

    private fun actionMenuCallback() {
        val actionCallback = object : ActionMode.Callback{
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                //стираем меню, когда оно пытается нарисоваться
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                //стираем меню, когда оно пытается нарисоваться
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, menu: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

        }
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }

    private fun setTextSize() = with(binding) {
        edTitle.setTextSize(pref?.getString("title_size_key", "20"))
        edDescription.setTextSize(pref?.getString("content_size_key", "16"))
    }

    private fun EditText.setTextSize(size: String?) {
        if(size != null) {
            this.textSize = size.toFloat()
        }
    }
}