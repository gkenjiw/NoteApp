package br.senac.noteapp.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.noteapp.databinding.ActivityNewNoteBinding
import br.senac.noteapp.model.Note
import br.senac.noteapp.model.Notes

class NewNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val sharedPrefs = getSharedPreferences("Users", Context.MODE_PRIVATE)
            val username = sharedPrefs.getString("username","default") as String

            val note = Note(binding.etTitle.text.toString(), binding.etDesc.text.toString(), username)

            Notes.noteList.add(note)

            finish()
        }
    }

}