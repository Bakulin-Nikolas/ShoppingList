package com.nikolas.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikolas.shoppinglist.R
import com.nikolas.shoppinglist.activities.MainApp
import com.nikolas.shoppinglist.activities.NewNoteActivity
import com.nikolas.shoppinglist.databinding.FragmentNoteBinding
import com.nikolas.shoppinglist.db.MainViewModel
import com.nikolas.shoppinglist.db.NoteAdapter
import com.nikolas.shoppinglist.entities.NoteItem


class NoteFragment : BaseFragment() {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter

    private val mainViewModel: MainViewModel by activityViewModels{
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun OnClickNew() {
        val i = Intent(activity, NewNoteActivity::class.java)
        editLauncher.launch(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {
        //в контекст надо передать активити, так как мы во фрагменте
        rcViewNote.layoutManager = LinearLayoutManager(activity)
        adapter = NoteAdapter()
        rcViewNote.adapter = adapter
    }


    private fun observer() {
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
            }
        }
    }

    companion object {
        const val NEW_NOTE_KEY = "new_note_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}