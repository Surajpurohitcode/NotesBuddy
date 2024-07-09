package com.surajpurohit.notesbuddy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.surajpurohit.notesbuddy.R
import com.surajpurohit.notesbuddy.data.adapter.NoteAdapter
import com.surajpurohit.notesbuddy.data.model.Note
import com.surajpurohit.notesbuddy.data.viewmodel.NoteViewModel
import com.surajpurohit.notesbuddy.data.viewmodel.NoteViewModelFactory
import com.surajpurohit.notesbuddy.databinding.FragmentHomeBinding
import com.surajpurohit.notesbuddy.repository.NoteRepository

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: NoteAdapter
    private lateinit var noteList: List<Note>
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var viewModelFactory: NoteViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            binding.userName.text = auth.currentUser!!.displayName
            Glide.with(this).load(auth.currentUser!!.photoUrl).into(binding.userProfilePic)
        }

        binding.addNewNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        val repository = NoteRepository(requireContext())
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        binding.noteRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        noteList = ArrayList<Note>()

        adapter = NoteAdapter(requireContext(), noteList)

        binding.noteRecyclerView.adapter = adapter

        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                adapter.updateNotes(it)
            }
        }

        return binding.root
    }

}