package com.surajpurohit.notesbuddy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.surajpurohit.notesbuddy.R
import com.surajpurohit.notesbuddy.databinding.FragmentFullNoteBinding

class FullNoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFullNoteBinding.inflate(inflater,container,false)

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_fullNoteFragment_to_homeFragment)
        }
        val noteID = arguments?.getLong("noteID")
        val noteTitle = arguments?.getString("noteTitle") ?:""
        val noteDisp = arguments?.getString("noteDisp") ?: ""
        val bundle = Bundle()
        if (noteID != null) {
            bundle.putLong("noteId",noteID)
            bundle.putString("noteTitle",noteTitle)
            bundle.putString("noteDisp",noteDisp)
        }


        binding.updateNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_fullNoteFragment_to_updateNoteFragment,bundle)
        }

        binding.noteTitle.setText(arguments?.getString("noteTitle") ?:"" )
        binding.noteDisp.setText(arguments?.getString("noteDisp") ?: "")

        return binding.root
    }

}