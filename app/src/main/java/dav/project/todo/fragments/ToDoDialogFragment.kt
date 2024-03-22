package dav.project.todo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import dav.project.todo.databinding.FragmentToDoDialogBinding
import dav.project.todo.utils.ToDoData


class ToDoDialogFragment : DialogFragment() {

    private lateinit var binding:FragmentToDoDialogBinding
    private var listener : OnDialogNextBtnClickListener? = null
    private var toDoData: ToDoData? = null


    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogFragment"
        @JvmStatic
        fun newInstance(taskId: String, task: String) =
            ToDoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                }
            }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentToDoDialogBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){

            toDoData = ToDoData(arguments?.getString("taskId").toString() ,arguments?.getString("task").toString())
            binding.etTodo.setText(toDoData?.task)
        }


        binding.todoClose.setOnClickListener {
            dismiss()
        }

        binding.btnAddTodo.setOnClickListener {

            val todoTask = binding.etTodo.text.toString()
            if (todoTask.isNotEmpty()){
                if (toDoData == null){
                    listener?.saveTask(todoTask , binding.etTodo)
                }else{
                    toDoData!!.task = todoTask
                    listener?.updateTask(toDoData!!, binding.etTodo)
                }

            }
        }
    }

    interface OnDialogNextBtnClickListener{
        fun saveTask(todoTask:String , todoEdit: TextInputEditText)
        fun updateTask(toDoData: ToDoData , todoEdit:TextInputEditText)
    }

}