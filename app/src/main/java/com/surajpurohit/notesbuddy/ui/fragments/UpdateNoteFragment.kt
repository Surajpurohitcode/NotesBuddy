package com.surajpurohit.notesbuddy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.surajpurohit.notesbuddy.R
import com.surajpurohit.notesbuddy.data.model.Note
import com.surajpurohit.notesbuddy.data.viewmodel.NoteViewModel
import com.surajpurohit.notesbuddy.data.viewmodel.NoteViewModelFactory
import com.surajpurohit.notesbuddy.databinding.FragmentUpdateNoteBinding
import com.surajpurohit.notesbuddy.repository.NoteRepository
import com.surajpurohit.notesbuddy.utils.Helper
import java.util.Date

class UpdateNoteFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var viewModelFactory: NoteViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
        }

        binding.showStyleBarButton.setOnClickListener {
            binding.stylesbar.visibility = View.VISIBLE
            binding.noteDispInput.setStylesBar(binding.stylesbar)
        }

        val repository = NoteRepository(requireContext())
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        binding.noteTitleInput.setText(arguments?.getString("noteTitle"))
        binding.noteDispInput.setText(arguments?.getString("noteDisp"))

        binding.updateButton.setOnClickListener {
            val noteTitle = binding.noteTitleInput.text.toString()
            val noteDisp = binding.noteDispInput.text.toString()
            val date = Date()

            // Retrieve the note ID from fragment arguments
            val noteId = arguments?.getLong("noteId") ?: -1

            // Make sure the noteId is valid
            if (noteId.toInt() != -1) {
                val note = Note(noteId, noteTitle, noteDisp, Helper.formatDate(date))

                // Update the note using ViewModel
                val updatedRows = noteViewModel.update(note)

                if (updatedRows > 0) {
                    Toast.makeText(
                        requireActivity(),
                        "Note updated successfully!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireActivity(), "Failed to update note.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireActivity(), "Invalid Note ID.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val date = Date()
        binding.updateNoteDateTextView.setText(Helper.formatDate(date))




        return binding.root
    }

}