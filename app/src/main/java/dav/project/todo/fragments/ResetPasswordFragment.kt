package dav.project.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import dav.project.todo.R
import dav.project.todo.databinding.FragmentResetPasswordBinding
import dav.project.todo.databinding.FragmentSignInBinding


class ResetPasswordFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentResetPasswordBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View){

        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents(){

        binding.tvSignIn.setOnClickListener{
            navControl.navigate( R.id.action_resetPasswordFragment_to_signInFragment)
        }
        binding.tvSignUp.setOnClickListener{
            navControl.navigate(R.id.action_resetPasswordFragment_to_signUpFragment)
        }

        binding.btnReset.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()

            if (email.isNotEmpty()) {

                auth.sendPasswordResetEmail(email).addOnCompleteListener{

                    if (it.isSuccessful){
                        Toast.makeText(requireContext(), "Check your inbox", Toast.LENGTH_SHORT).show()
                        navControl.navigate(R.id.action_signUpFragment_to_signInFragment)
                    }else{
                        Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

}

