package com.yourname.englishlearning.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.TaskType

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setupButtons()
    }

    private fun setupButtons() {
        // Кнопка для всех типов заданий
        findViewById<Button>(R.id.btnAllTasks).setOnClickListener {
            startCourseActivity(TaskType.ALL)
        }

        // Кнопка для заданий "Да/Нет"
        findViewById<Button>(R.id.btnYesNoTasks).setOnClickListener {
            startCourseActivity(TaskType.YES_NO)
        }

        // Кнопка для заданий на правописание
        findViewById<Button>(R.id.btnSpellingTasks).setOnClickListener {
            startCourseActivity(TaskType.SPELLING)
        }

        // Кнопка для заданий на сопоставление
        findViewById<Button>(R.id.btnMatchingTasks).setOnClickListener {
            startCourseActivity(TaskType.MATCHING)
        }
    }

    private fun startCourseActivity(taskType: TaskType) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("TASK_TYPE", taskType.name)
        }
        startActivity(intent)
    }
}
