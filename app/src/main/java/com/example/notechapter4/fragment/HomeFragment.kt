package com.example.notechapter4.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.notechapter4.MainActivity
import com.example.notechapter4.R
import com.example.notechapter4.adapter.NoteAdapter
import com.example.notechapter4.databinding.EditDialogBinding
import com.example.notechapter4.databinding.FragmentHomeBinding
import com.example.notechapter4.databinding.FragmentLoginBinding
import com.example.notechapter4.room.ApplicationDatabase
import com.example.notechapter4.room.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private var mDB: ApplicationDatabase? = null
    private var adapter: NoteAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDB = ApplicationDatabase.getInstance(requireContext())
        //Set Username
        val getSharedPreferences = requireContext().getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        fetchData()
        binding.usernameTextView.text = "Welcome ${getSharedPreferences?.getString("email",null)}"
        binding.addButton.setOnClickListener {
            val dialog = AddFragment{
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = mDB?.noteDao()?.insertNote(it)
                    activity?.runOnUiThread {
                        if (result==(0).toLong()){
                            Toast.makeText(context,"FAILED INSERT DATA",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(context,"SUKSES DATA",Toast.LENGTH_LONG).show()
                            fetchData()
                        }
                    }
                }
            }
            dialog.show(parentFragmentManager,"dialog")
        }
    }
    fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO) {
            val myDB = mDB?.noteDao()
            val listUser = myDB?.getAllNote()
            activity?.runOnUiThread {
                listUser?.let {
                    adapter = NoteAdapter(it, delete = {
                            User ->
                        AlertDialog.Builder(requireContext()).setPositiveButton("Iya"){_,_ ->
                            val mDb = ApplicationDatabase.getInstance(requireContext())
                            lifecycleScope.launch(Dispatchers.IO){
                                val result = mDb?.noteDao()?.deleteNote(User)
                                activity?.runOnUiThread {
                                    if (result != 0){
                                        Toast.makeText(
                                            requireContext(),
                                            "Data ${User.tittle} berhasil dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Data ${User.tittle} gagal dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    fetchData()
                                }

                            }
                        }.setNegativeButton("Tidak"){dialog, _ ->
                            dialog.dismiss()
                        }
                        .setMessage("Aoakah anda yakin ingin menghapus ${User.tittle}")
                        .setTitle("Konfirmasi Hapus")
                        .create()
                        .show()
                    }
                        , edit = { data ->
                        val dialogBinding = EditDialogBinding.inflate(LayoutInflater.from(requireContext()))
                        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        dialogBuilder.setView(dialogBinding.root)
                        val dialog = dialogBuilder.create()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialogBinding.titleEditText.setText("${data.tittle}")
                        dialogBinding.noteEditText.setText("${data.note}")
                        dialogBinding.btnSave.setOnClickListener {
                            val mDB = ApplicationDatabase.getInstance(requireContext())
                            data.tittle  = dialogBinding.titleEditText.text.toString()
                            data.note = dialogBinding.noteEditText.text.toString()
                            lifecycleScope.launch(Dispatchers.IO){
                                val result = mDB?.noteDao()?.updateNote(data)
                                runBlocking(Dispatchers.Main){
                                    if (result != 0){
                                        Toast.makeText(
                                            requireContext(),
                                            "${data.tittle} Berhasil di update!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        fetchData()
                                        dialog.dismiss()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "${data.tittle} Gagal di update!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog.dismiss()
                                    }
                                }
                            }
                        }
                        dialog.show()
                    }
                    )
                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        fetchData()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}