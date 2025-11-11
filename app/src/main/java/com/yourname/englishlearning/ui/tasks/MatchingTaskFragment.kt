package com.yourname.englishlearning.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.Task
import com.yourname.englishlearning.data.MatchingTask

class MatchingTaskFragment : BaseTaskFragment() {

    private lateinit var questionText: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button

//    Создание интерфейса
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_matching, container, false)

        questionText = view.findViewById(R.id.questionText)
        option1Button = view.findViewById(R.id.option1Button)
        option2Button = view.findViewById(R.id.option2Button)
        option3Button = view.findViewById(R.id.option3Button)

        // Загружаем задание из аргументов
        arguments?.getSerializable("task")?.let { task ->
            if (task is MatchingTask) {
                initializeWithTask(task)
            }
        }

        return view
    }

//    Настройка задания
    override fun initializeWithTask(task: Task) {
        super.initializeWithTask(task)

        if (task is MatchingTask && ::questionText.isInitialized && ::option1Button.isInitialized) {
            questionText.text = "Выберите правильный перевод для:\n${task.englishWord}"

            // Устанавливаем варианты ответов
            option1Button.text = task.options[0]
            option2Button.text = task.options[1]
            option3Button.text = task.options[2]

            // Устанавливаем обработчики
            option1Button.setOnClickListener { submitAnswer(task.options[0]) }
            option2Button.setOnClickListener { submitAnswer(task.options[1]) }
            option3Button.setOnClickListener { submitAnswer(task.options[2]) }
        }
    }

    private fun submitAnswer(answer: String) {
        onAnswerSubmitted?.invoke(answer)
    }
}
