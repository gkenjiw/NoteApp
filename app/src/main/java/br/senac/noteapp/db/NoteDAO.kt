package br.senac.noteapp.db

import androidx.room.*
import br.senac.noteapp.model.Note

@Dao
interface NoteDAO {
    @Insert
    fun save(note : Note)

    @Query(value = "Select * from Note")
    fun getAll() : List<Note>

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}