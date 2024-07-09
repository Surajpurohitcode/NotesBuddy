package com.surajpurohit.notesbuddy.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.surajpurohit.notesbuddy.data.model.Note
import com.surajpurohit.notesbuddy.repository.NoteRepository

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>>
        get() = _notes

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _notes.value = repository.getAllNotes()
    }

    fun insert(note: Note): Long {
        return repository.insert(note)
    }

    fun update(note: Note): Int {
        return repository.update(note)
    }

    fun delete(note: Note): Int {
        return repository.delete(note)
    }
}