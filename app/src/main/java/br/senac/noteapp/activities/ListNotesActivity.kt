package br.senac.noteapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityListNotesBinding
import br.senac.noteapp.databinding.NoteCardBinding
import br.senac.noteapp.model.Note
import br.senac.noteapp.model.Notes

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

    fun refreshNotes() {
        binding.noteContainer.removeAllViews()

        val prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        val color = prefManager.getInt("noteColor", R.color.noteDefaultColor)

        Notes.noteList.forEach {
            val cardBinding = NoteCardBinding.inflate(layoutInflater)

            cardBinding.txtTitle.text = it.title
            cardBinding.txtDesc.text = it.desc
            cardBinding.txtUser.text = it.user

            cardBinding.root.setCardBackgroundColor(color)

            binding.noteContainer.addView(cardBinding.root)
        }
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