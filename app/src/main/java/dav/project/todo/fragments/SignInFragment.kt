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
import dav.project.todo.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSignInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
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


        binding.tvSignUp.setOnClickListener{
            navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.tvForgotPass.setOnClickListener{
            navControl.navigate(R.id.action_signInFragment_to_resetPasswordFragment)
        }

        binding.btnSignUp.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPass.text.toString().trim()


            if (email.isNotEmpty() && password.isNotEmpty()) {

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { registrationTask ->

                            val user = FirebaseAuth.getInstance().currentUser

                            if (registrationTask.isSuccessful) {

                                Toast.makeText(
                                    requireContext(),
                                    "Please verify your Email",
                                    Toast.LENGTH_SHORT
                                ).show()

                                if (user?.isEmailVerified == true){
                                    navControl.navigate(R.id.action_signInFragment_to_homeFragment)
                                }


                            } else {
                                Toast.makeText(requireContext(), registrationTask.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                } else {
                Toast.makeText(requireContext(), "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

