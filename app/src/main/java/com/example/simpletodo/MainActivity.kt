package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.readLines
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTasks.removeAt(position)

                // Notify the adapter of change
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Create another passing in the sampler user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // Attach the adapter to the recyclerView to populate items
        recyclerView.adapter = adapter

        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field so the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        findViewById<Button>(R.id.button).setOnClickListener {
            // Grab the text the user inputted
            val userInputtedTask = inputTextField.text.toString()

            // Add the string ti our list of tasks
            listOfTasks.add(userInputtedTask)

            // Notify the adapter of updated data
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the user has inputted
    // By writing and reading from a file

    // Get the file
    fun getDataFile() : File {

        // Every line represents a specific task
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into the data file
    fun saveItems() {
        try {
            writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}