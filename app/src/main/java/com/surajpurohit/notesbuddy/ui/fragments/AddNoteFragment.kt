package com.surajpurohit.notesbuddy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.surajpurohit.notesbuddy.R
import com.surajpurohit.notesbuddy.data.model.Note
import com.surajpurohit.notesbuddy.data.viewmodel.NoteViewModel
import com.surajpurohit.notesbuddy.data.viewmodel.NoteViewModelFactory
import com.surajpurohit.notesbuddy.databinding.FragmentAddNoteBinding
import com.surajpurohit.notesbuddy.db.NotesDatabaseHelper
import com.surajpurohit.notesbuddy.repository.NoteRepository
import com.surajpurohit.notesbuddy.utils.Helper
import java.util.Date

class AddNoteFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var viewModelFactory:NoteViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_addNoteFragment_to_homeFragment)
        }

        binding.showStyleBarButton.setOnClickListener {
            binding.stylesbar.visibility = View.VISIBLE
            binding.noteDispInput.setStylesBar(binding.stylesbar)
        }

        val repository = NoteRepository(requireContext())
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        binding.doneButton.setOnClickListener {
            val noteTitle = binding.noteTitleInput.text.toString()
            val noteDisp = binding.noteDispInput.text.toString()
            val date = Date()
            val note = Note(0, noteTitle, noteDisp, Helper.formatDate(date))
            val insertedId = noteViewModel.insert(note)

            if (insertedId != -1L) {
                Toast.makeText(requireActivity(), "Note inserted successfully!", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_addNoteFragment_to_homeFragment)

            } else {
                Toast.makeText(requireActivity(), "Failed to insert note.", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        val date = Date()
        binding.noteDateTextView.setText(Helper.formatDate(date))

        return binding.root
    }

}