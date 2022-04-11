package com.example.notechapter4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.notechapter4.R
import com.example.notechapter4.databinding.FragmentLoginBinding
import com.example.notechapter4.databinding.FragmentRegisterBinding
import com.example.notechapter4.room.ApplicationDatabase
import com.example.notechapter4.room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    //Binding
    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    //mDB
    private var mDb: ApplicationDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = ApplicationDatabase.getInstance(requireContext())

        Glide.with(this)
            .load("https://i.ibb.co/zJHYGBP/binarlogo.jpg")

            .into(binding.logoImageView)

        //EVENT MAKE ACCOUNT
        binding.signUpButton.setOnClickListener {
            val username            = binding.inputUsernameEditText.text.toString()
            val email               = binding.inputEmailEditText.text.toString()
            val password            = binding.inputPasswordEditText.text.toString()
            val confirmPassword     = binding.inputConfirmPasswordEditText.text.toString()
            val registerCheckEmpty = User(null,username,email,password)
            when{
                username.isNullOrEmpty() -> {
                    binding.materialUsername.error = "Required Input"
                }
                email.isNullOrEmpty() -> {
                    binding.materialEmail.error = "Required Input"
                }
                password.isNullOrEmpty() -> {
                    binding.materialPassword.error = "Required Input"
                }
                confirmPassword.isNullOrEmpty() -> {
                    binding.materialConfirmPassword.error = "Required Input"
                }
            //Cek Password & Confirm Password is same
                password.lowercase() != confirmPassword.lowercase() ->{
                    binding.materialConfirmPassword.error = "Password and Confirm Password are not the same"
                    binding.inputConfirmPasswordEditText.setText("")
                }else->{
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = mDb?.userDao()?.insertUserAccount(registerCheckEmpty)
                        activity?.runOnUiThread {
                            if (result != 0.toLong()){
                                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                }
            }
        }
    }
}