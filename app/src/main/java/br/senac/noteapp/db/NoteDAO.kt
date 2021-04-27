package br.senac.noteapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.senac.noteapp.model.Note

@Dao
interface NoteDAO {
    @Insert
    fun save(note : Note)

    @Query(value = "Select * from Note")
    fun getAll() : List<Note>
}