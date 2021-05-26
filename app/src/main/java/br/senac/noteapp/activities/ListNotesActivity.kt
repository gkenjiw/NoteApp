package br.senac.noteapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityListNotesBinding
import br.senac.noteapp.databinding.NoteCardBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note
import br.senac.noteapp.model.Notes
import java.math.RoundingMode

class ListNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityListNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val i = Intent(this, NewNoteActivity::class.java)
            startActivity(i)
        }
    }

    fun updateUI(notes : List<Note>) {
        binding.noteContainer.removeAllViews()

        val prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        val color = prefManager.getInt("noteColor", R.color.noteDefaultColor)
        val textColor = prefManager.getInt("noteTextColor", R.color.noteTextDefaultColor)
        var titleTextSize = prefManager.getString("noteTitleTextSize", "24")!!.toFloatOrNull()
        if (titleTextSize == null) titleTextSize = 24f
        var bodyTextSize = prefManager.getString("noteBodyTextSize", "14")!!.toFloatOrNull()
        if (bodyTextSize == null) bodyTextSize = 14f

        //Notes.noteList.forEach {
        notes.forEach {
            val cardBinding = NoteCardBinding.inflate(layoutInflater)
            val note = it

            cardBinding.txtTitle.text = note.title
            cardBinding.txtDesc.text = note.desc
            cardBinding.txtUser.text = note.user

            cardBinding.root.setCardBackgroundColor(color)

            cardBinding.txtTitle.textSize = titleTextSize
            cardBinding.txtDesc.textSize = bodyTextSize

            cardBinding.txtTitle.setTextColor(textColor)
            cardBinding.txtDesc.setTextColor(textColor)

            cardBinding.btnEdit.setOnClickListener {
                val i = Intent(this, NewNoteActivity::class.java)
                i.putExtra("isEdit", true)
                i.putExtra("id", note.id)
                i.putExtra("title", note.title)
                i.putExtra("desc", note.desc)

                startActivity(i)
            }

            cardBinding.btnDelete.setOnClickListener {
                val noteToDel = Note(id = note.id, title = note.title, desc = note.desc, user = note.user)
                Thread {
                    deleteNote(noteToDel)
                    refreshNotes()
                }.start()
            }

            binding.noteContainer.addView(cardBinding.root)
        }
    }

    fun refreshNotes() {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()
        Thread {
            val notes = db.noteDao().getAll()

            runOnUiThread {
                updateUI(notes)
            }
            
        }.start()
    }

    fun deleteNote(note : Note) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()

        db.noteDao().delete(note)
    }

    override fun onResume() {
        super.onResume()
        refreshNotes()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.user -> {
                val i = Intent(this, UserActivity::class.java)
                startActivity(i)
            }
            R.id.config -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}