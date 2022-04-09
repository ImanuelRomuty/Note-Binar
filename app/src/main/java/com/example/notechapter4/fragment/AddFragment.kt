package com.example.notechapter4.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.notechapter4.R
import com.example.notechapter4.databinding.FragmentAddBinding
import com.example.notechapter4.room.ApplicationDatabase
import com.example.notechapter4.room.Note
import com.example.notechapter4.room.User

class AddFragment(private val listNote : (Note)->Unit) : DialogFragment() {
    private var _binding: FragmentAddBinding? = null
    private var mDb: ApplicationDatabase? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)
        mDb = ApplicationDatabase.getInstance(requireContext())
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            binding.apply {
                btnSave.setOnClickListener {
                    val title : String = binding.etTitle.text.toString()
                    val note : String = binding.etNote.text.toString()
                    val user = Note(
                        null,title,note
                    )
                    listNote(user)
                    dialog?.dismiss()
                }
            }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}