import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import dav.project.todo.R
import dav.project.todo.databinding.FragmentLogOutDialogBinding

class LogOutDialogFragment : Fragment() {

    private lateinit var binding: FragmentLogOutDialogBinding
    private lateinit var navControl: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogOutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController
        navControl = Navigation.findNavController(view)

        auth = FirebaseAuth.getInstance()

        binding.btnLogOut.setOnClickListener {
            auth.signOut()

            navControl.navigate(R.id.action_logOutDialogFragment_to_signInFragment)
            Toast.makeText(requireContext(),"You have successfully logged out", Toast.LENGTH_SHORT).show()
        }

        binding.btnGoBack.setOnClickListener{
            navControl.navigate(R.id.action_logOutDialogFragment_to_homeFragment)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            binding.tvUserGmail.text = currentUser.email
        }
    }
}
