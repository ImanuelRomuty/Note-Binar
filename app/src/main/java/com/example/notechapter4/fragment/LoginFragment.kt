package com.example.notechapter4.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.notechapter4.MainActivity
import com.example.notechapter4.R
import com.example.notechapter4.databinding.FragmentLoginBinding
import com.example.notechapter4.room.ApplicationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    //Binding
    private var _binding: FragmentLoginBinding?=null
    private val binding get() = _binding!!
    //mDB
    private var mDb: ApplicationDatabase? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = ApplicationDatabase.getInstance(requireContext())
        val sharedPreferences = requireContext()
            .getSharedPreferences(MainActivity.SHARED_PREFERENCES,Context.MODE_PRIVATE)

        //SET GLIDE
        Glide.with(this)
            .load("https://i.ibb.co/zJHYGBP/binarlogo.jpg")
            .into(binding.logoImageView)


        //EVENT LOGIN
        binding.loginButton.setOnClickListener{
            val email = binding.inputEmailEditText.text.toString()
            val password = binding.inputPasswordEditText.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                val login = mDb?.userDao()?.login(email,password)
                activity?.runOnUiThread {
                    if (login==null){
                        Toast.makeText(context, "Username atau Password Anda Salah", Toast.LENGTH_SHORT).show()
                    }else{
                        val editor = sharedPreferences.edit()
                        editor.putString("email",email)
                        editor.apply()
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment2())
                    }
                }
            }
        }

        //EVENT SIGN UP
        binding.registerButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}