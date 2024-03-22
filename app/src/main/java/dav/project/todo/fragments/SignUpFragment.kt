package dav.project.todo.fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import dav.project.todo.R
import dav.project.todo.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var Auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)

        binding.tvSignIn.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()
            val confirmPassword = binding.etConfirmPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.equals(confirmPassword)) {

                    Auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { registrationTask ->

                            val user = FirebaseAuth.getInstance().currentUser

                            if (registrationTask.isSuccessful) {

                                Toast.makeText(requireContext()
                                    ,
                                    "Please verify your Email",
                                    Toast.LENGTH_SHORT
                                ).show()


                                Auth.currentUser?.sendEmailVerification()

                                navController.navigate(R.id.action_signUpFragment_to_signInFragment)

                            } else {
                                Toast.makeText(requireContext(),registrationTask.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                } else {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun registerUser(email: String, pass: String) {
        Auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful)
                navController.navigate(R.id.action_signUpFragment_to_signInFragment)
            else
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        Auth = FirebaseAuth.getInstance()
    }
}


