package com.example.notechapter4.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.notechapter4.MainActivity.Companion.SHARED_PREFERENCES
import com.example.notechapter4.R
import com.example.notechapter4.databinding.FragmentAddBinding
import com.example.notechapter4.databinding.FragmentLoadingBinding
import com.example.notechapter4.databinding.FragmentLoginBinding
import com.example.notechapter4.room.ApplicationDatabase


class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private var mDb: ApplicationDatabase? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //SET GLIDE
        Glide.with(this)
            .load("https://i.ibb.co/zJHYGBP/binarlogo.jpg")
            .into(binding.loadingImageView)
        val sharedPreference = context?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val sharedPreferences = sharedPreference?.getString("email","")
//        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPreferences == ""){
//                val direct = L.actionSplashFragmentToLoginFragment()
                findNavController().navigate(LoadingFragmentDirections.actionLoadingFragmentToLoginFragment())
            }else{
//                val direct = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(LoadingFragmentDirections.actionLoadingFragmentToHomeFragment())
            }
//        }
//    ,10000)
    }


}