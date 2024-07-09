package com.surajpurohit.notesbuddy.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.surajpurohit.notesbuddy.data.model.Note
import com.surajpurohit.notesbuddy.db.NotesDatabaseHelper

class NoteRepository(context: Context) {
    private val dbHelper = NotesDatabaseHelper(context)

    @SuppressLint("Range")
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM ${NotesDatabaseHelper.TABLE_NOTES}", null)

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(NotesDatabaseHelper.COLUMN_ID))
                val title = it.getString(it.getColumnIndex(NotesDatabaseHelper.COLUMN_TITLE))
                val content = it.getString(it.getColumnIndex(NotesDatabaseHelper.COLUMN_CONTENT))
                val date = it.getString(it.getColumnIndex(NotesDatabaseHelper.COLUMN_DATE))
                notes.add(Note(id, title, content, date))
            }
        }

        cursor?.close()
        db.close()
        return notes
    }

    fun insert(note: Note): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(NotesDatabaseHelper.COLUMN_TITLE, note.title)
            put(NotesDatabaseHelper.COLUMN_CONTENT, note.content)
            put(NotesDatabaseHelper.COLUMN_DATE,note.date)
        }
        val id = db.insert(NotesDatabaseHelper.TABLE_NOTES, null, contentValues)
        db.close()
        return id
    }

    fun update(note: Note): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put(NotesDatabaseHelper.COLUMN_TITLE, note.title)
            put(NotesDatabaseHelper.COLUMN_CONTENT, note.content)
            put(NotesDatabaseHelper.COLUMN_DATE,note.date)
        }
        val updatedRows = db.update(NotesDatabaseHelper.TABLE_NOTES, contentValues, "${NotesDatabaseHelper.COLUMN_ID} = ?", arrayOf(note.id.toString()))
        db.close()
        return updatedRows
    }

    fun delete(note: Note): Int {
        val db = dbHelper.writableDatabase
        val deletedRows = db.delete(NotesDatabaseHelper.TABLE_NOTES, "${NotesDatabaseHelper.COLUMN_ID} = ?", arrayOf(note.id.toString()))
        db.close()
        return deletedRows
    }


}