package com.yourname.englishlearning.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.Task
import com.yourname.englishlearning.data.TaskType
import com.yourname.englishlearning.data.YesNoTask
import com.yourname.englishlearning.data.SpellingTask
import com.yourname.englishlearning.data.MatchingTask
import com.yourname.englishlearning.ui.results.ResultsAdapter
import com.yourname.englishlearning.ui.results.ResultItem

class ResultsActivity : AppCompatActivity() {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var scoreText: TextView
    private lateinit var messageText: TextView
    private lateinit var retryButton: Button
    private lateinit var menuButton: Button

    private lateinit var tasks: List<Task>
    private lateinit var userAnswers: List<Pair<Int, String>>
    private lateinit var courseType: TaskType
    private var percentage: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        initViews()
        loadDataFromIntent()
        setupRecyclerView()
        setupUI()
        setupButtons()
    }

    private fun initViews() {
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        scoreText = findViewById(R.id.scoreText)
        messageText = findViewById(R.id.messageText)
        retryButton = findViewById(R.id.retryButton)
        menuButton = findViewById(R.id.menuButton)
    }

    private fun loadDataFromIntent() {
        tasks = intent.getSerializableExtra("TASKS") as? List<Task> ?: emptyList()
        userAnswers = intent.getSerializableExtra("USER_ANSWERS") as? List<Pair<Int, String>> ?: emptyList()
        courseType = TaskType.valueOf(intent.getStringExtra("COURSE_TYPE") ?: "ALL")

        // Вычисляем процент правильных ответов на основе реальных проверок
        val correctCount = tasks.count { task ->
            val userAnswer = userAnswers.find { it.first == task.id }?.second ?: ""
            isAnswerCorrect(task, userAnswer)
        }
        percentage = (correctCount.toDouble() / tasks.size) * 100
    }

    private fun setupRecyclerView() {
        val resultsList = tasks.map { task ->
            val userAnswer = userAnswers.find { it.first == task.id }?.second ?: "Нет ответа"
            val isCorrect = isAnswerCorrect(task, userAnswer)
            ResultItem(task, userAnswer, isCorrect)
        }

        val adapter = ResultsAdapter(resultsList)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = adapter
    }

    private fun isAnswerCorrect(task: Task, userAnswer: String): Boolean {
        return when (task) {
            is YesNoTask -> {
                // Для YesNoTask правильный ответ зависит от isCorrect
                val expectedAnswer = if (task.isCorrect) "да" else "нет"
                userAnswer.equals(expectedAnswer, ignoreCase = true)
            }
            is SpellingTask -> {
                // Для правописания сравниваем с correctLetter
                userAnswer.equals(task.correctLetter, ignoreCase = true)
            }
            is MatchingTask -> {
                // Для сопоставления сравниваем с correctAnswer
                userAnswer.equals(task.correctAnswer, ignoreCase = true)
            }
            else -> {
                // Для других типов (если будут добавлены) сравниваем с correctAnswer
                userAnswer.equals(task.correctAnswer, ignoreCase = true)
            }
        }
    }

    private fun setupUI() {
        val correctCount = tasks.count { task ->
            val userAnswer = userAnswers.find { it.first == task.id }?.second ?: ""
            isAnswerCorrect(task, userAnswer)
        }

        scoreText.text = "Результат: $correctCount/${tasks.size} (${"%.1f".format(percentage)}%)"

        if (percentage >= 70) {
            messageText.text = "Поздравляем! Курс пройден успешно! 🎉"
            messageText.setTextColor(ContextCompat.getColor(this, R.color.green))
            menuButton.isEnabled = true
            menuButton.alpha = 1f
        } else {
            messageText.text = "Нужно повторить курс. Попробуйте еще раз! 📚"
            messageText.setTextColor(ContextCompat.getColor(this, R.color.red))
            menuButton.isEnabled = false
            menuButton.alpha = 0.5f
        }
    }

    private fun setupButtons() {
        retryButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("TASK_TYPE", courseType.name)
            }
            startActivity(intent)
            finish()
        }

        menuButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
