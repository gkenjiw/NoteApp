package br.senac.noteapp.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import br.senac.noteapp.databinding.ActivityNewNoteBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note
import br.senac.noteapp.model.Notes
import kotlin.concurrent.thread

class NewNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("Users", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("username","default") as String

        if (intent.getBooleanExtra("isEdit", false)) {
            var titleEdit = intent.getStringExtra("title")
            var descEdit = intent.getStringExtra("desc")
            var id = intent.getIntExtra("id", 0)

            binding.etTitle.setText(titleEdit)
            binding.etDesc.setText(descEdit)

            binding.btnAdd.setOnClickListener {
                val note = Note(id = id, title = binding.etTitle.text.toString(), desc = binding.etDesc.text.toString(), user = username)

                Thread {
                    updateNote(note)
                    finish()
                }.start()
            }

        } else {
            binding.btnAdd.setOnClickListener {
                val note = Note(title = binding.etTitle.text.toString(), desc = binding.etDesc.text.toString(), user = username)

                //Notes.noteList.add(note)
                Thread {
                    saveNote(note)
                    finish()
                }.start()

                //finish()
            }
        }

    }

    fun saveNote(note : Note) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()

        db.noteDao().save(note)
    }

    fun updateNote(note : Note) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()

        db.noteDao().update(note)
    }

}