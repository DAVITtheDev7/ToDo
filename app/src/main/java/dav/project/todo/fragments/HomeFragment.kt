package dav.project.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dav.project.todo.R
import dav.project.todo.databinding.FragmentHomeBinding
import dav.project.todo.utils.ToDoAdapter
import dav.project.todo.utils.ToDoData

class HomeFragment : Fragment(), ToDoDialogFragment.OnDialogNextBtnClickListener,
    ToDoAdapter.TaskAdapterInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var databaseRef: DatabaseReference
    private lateinit var binding: FragmentHomeBinding
    private var PopUpFragment: ToDoDialogFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.ivAddTodo.setOnClickListener {
            PopUpFragment = ToDoDialogFragment()
            PopUpFragment!!.setListener(this)
            PopUpFragment!!.show(
                childFragmentManager,
                ToDoDialogFragment.TAG
            )
        }
        binding.ivUser.setOnClickListener{
            navControl.navigate(R.id.action_homeFragment_to_logOutDialogFragment)
        }
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear() // Clear the list before adding new tasks
                for (taskSnapshot in snapshot.children) {
                    val todoTask =
                        taskSnapshot.key?.let { ToDoData(it, taskSnapshot.value.toString()) }
                    todoTask?.let { mList.add(it) } // Add task only if it's not null
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteItemClicked(toDoData: ToDoData, position: Int) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun saveTask(todoTask: String, todoEdit: TextInputEditText) {
        if (!mList.any { it.task == todoTask }) {
            databaseRef.push().setValue(todoTask).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "ToDo added successfully!", Toast.LENGTH_SHORT)
                        .show()
                    todoEdit.text = null
                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Task already exists!", Toast.LENGTH_SHORT).show()
        }
        PopUpFragment?.dismiss()
    }

    override fun updateTask(toDoData: ToDoData, todoEdit: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        databaseRef.updateChildren(map)
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            PopUpFragment!!.dismiss()
        }
    }

    override fun onEditItemClicked(toDoData: ToDoData, position: Int) {
        if (PopUpFragment != null)
            childFragmentManager.beginTransaction().remove(PopUpFragment!!).commit()

        PopUpFragment = ToDoDialogFragment.newInstance(toDoData.taskId, toDoData.task)
        PopUpFragment!!.setListener(this)
        PopUpFragment!!.show(
            childFragmentManager,
            ToDoDialogFragment.TAG
        )
    }
}
