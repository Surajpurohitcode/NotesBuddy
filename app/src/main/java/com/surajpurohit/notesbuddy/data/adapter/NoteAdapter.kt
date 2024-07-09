package com.surajpurohit.notesbuddy.data.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.surajpurohit.notesbuddy.R
import com.surajpurohit.notesbuddy.data.model.Note
import com.surajpurohit.notesbuddy.databinding.NotesListLayoutBinding

class NoteAdapter(val context: Context,var noteList: List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = NotesListLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notes_list_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {

        var navController: NavController? = null

        val bundle = Bundle()

        holder.binding.noteListTitle.setText(noteList.get(position).title)
        holder.binding.noteListDisp.setText(noteList.get(position).content)
        holder.binding.noteListDate.setText(noteList.get(position).date)

        bundle.putString("noteTitle",noteList.get(position).title)
        bundle.putString("noteDisp",noteList.get(position).content)
        bundle.putLong("noteID",noteList.get(position).id)

        holder.binding.noteListCard.setOnClickListener {
            navController = Navigation.findNavController(holder.itemView)
            navController!!.navigate(R.id.action_homeFragment_to_fullNoteFragment,bundle)
        }

        holder.binding.noteShareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, noteList.get(position).content)
                putExtra(Intent.EXTRA_TITLE,noteList.get(position).title)
                type = "text/plain"
            }

            context.startActivity(Intent.createChooser(shareIntent, null))


        }
    }

    fun updateNotes(newNotes: List<Note>) {
        noteList = newNotes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}