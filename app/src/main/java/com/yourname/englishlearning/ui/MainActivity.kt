package com.yourname.englishlearning.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.*
import com.yourname.englishlearning.ui.tasks.BaseTaskFragment
import com.yourname.englishlearning.ui.tasks.YesNoTaskFragment
import com.yourname.englishlearning.ui.tasks.SpellingTaskFragment
import com.yourname.englishlearning.ui.tasks.MatchingTaskFragment

class MainActivity : AppCompatActivity() {

    private lateinit var repository: TaskRepository
    private lateinit var tasks: List<Task>
    private var currentTaskIndex = 0
    private val userAnswers = mutableListOf<Pair<Int, String>>() // Храним ID задания и ответ пользователя
    private lateinit var currentCourseType: TaskType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Добавляем кнопку возврата в меню
        findViewById<Button>(R.id.btnBackToMenu).setOnClickListener {
            finish() // Возвращаемся в главное меню
        }

        try {
            // Созд репо
            repository = TaskRepository(this)

            // Получаем тип курса
            currentCourseType = intent.getStringExtra("TASK_TYPE")?.let {
                TaskType.valueOf(it)
            } ?: TaskType.ALL

            startNewSession()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка инициализации: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            finish() // Возвращаемся в меню
        }
    }

    // Начало новой сессии
    private fun startNewSession() {
        try {
//            Выбор метода генерации задания в зависимости от курса
            tasks = when (currentCourseType) {
                TaskType.ALL -> getAllTasksMixed()
                TaskType.YES_NO -> getYesNoTasksOnly()
                TaskType.SPELLING -> getSpellingTasksOnly()
                TaskType.MATCHING -> getMatchingTasksOnly()
            }

            currentTaskIndex = 0
            userAnswers.clear()

            if (tasks.isEmpty()) {
                Toast.makeText(this, "Нет заданий для выполнения", Toast.LENGTH_LONG).show()
                return
            }

            showNextTask()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка загрузки заданий: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun getAllTasksMixed(): List<Task> {
        val yesNoTasks = repository.generateYesNoTasks().shuffled().take(3)
        val spellingTasks = repository.generateSpellingTasks().shuffled().take(3)
        val matchingTasks = repository.generateMatchingTasks().shuffled().take(3)
        return (yesNoTasks + spellingTasks + matchingTasks).shuffled()
    }

    private fun getYesNoTasksOnly(): List<Task> {
        return repository.generateYesNoTasks().shuffled().take(10)
    }

    private fun getSpellingTasksOnly(): List<Task> {
        return repository.generateSpellingTasks().shuffled().take(10)
    }

    private fun getMatchingTasksOnly(): List<Task> {
        return repository.generateMatchingTasks().shuffled().take(10)
    }

    //    отображение следующего задания
    private fun showNextTask() {
        if (currentTaskIndex >= tasks.size) {
            showResults()
            return
        }

        val currentTask = tasks[currentTaskIndex]
        val fragment = createFragmentForTask(currentTask)

        // Устанавливаем callback
        fragment.onAnswerSubmitted = { userAnswer ->
            // Сохраняем реальный ответ пользователя
            userAnswers.add(Pair(currentTask.id, userAnswer))
            currentTaskIndex++
            showNextTask()
        }

        // Передаем задание в фрагмент через аргументы
        val args = Bundle().apply {
            putSerializable("task", currentTask)
        }
        fragment.arguments = args

        // Отображаем фрагмент
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun createFragmentForTask(task: Task): BaseTaskFragment {
        return when (task) {
            is YesNoTask -> YesNoTaskFragment()
            is SpellingTask -> SpellingTaskFragment()
            is MatchingTask -> MatchingTaskFragment()
            else -> YesNoTaskFragment()
        }
    }

    private fun showResults() {
        val intent = Intent(this, ResultsActivity::class.java).apply {
            putExtra("TASKS", ArrayList(tasks))
            putExtra("USER_ANSWERS", ArrayList(userAnswers))
            putExtra("COURSE_TYPE", currentCourseType.name)
        }
        startActivity(intent)
        finish()
    }
}
