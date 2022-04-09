package com.example.notechapter4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notechapter4.databinding.NoteItemBinding
import com.example.notechapter4.room.Note

class NoteAdapter(
    private val listNote: List<Note>,
    //callback unit
    private val delete: (Note) -> Unit,
    private val edit : (Note) -> Unit
    ) : RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

    class ViewHolder(val binding : NoteItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tittleTextView.text = listNote[position].tittle
            noteTextView.text  = listNote[position].note

            editButton.setOnClickListener {
                edit.invoke(listNote[position])
            }
            deleteButton.setOnClickListener {
                delete.invoke(listNote[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }
}