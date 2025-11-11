package com.yourname.englishlearning.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.Task
import com.yourname.englishlearning.data.SpellingTask

class SpellingTaskFragment : BaseTaskFragment() {

    private lateinit var questionText: TextView
    private lateinit var hintText: TextView
    private lateinit var answerInput: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spelling, container, false)

        questionText = view.findViewById(R.id.questionText)
        hintText = view.findViewById(R.id.hintText)
        answerInput = view.findViewById(R.id.answerInput)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val answer = answerInput.text.toString().trim()
            if (answer.isNotEmpty()) {
                submitAnswer(answer)
            } else {
                answerInput.error = "Введите букву"
            }
        }

        // Загружаем задание из аргументов
        arguments?.getSerializable("task")?.let { task ->
            if (task is SpellingTask) {
                initializeWithTask(task)
            }
        }

        return view
    }
//Настройка задания
    override fun initializeWithTask(task: Task) {
        super.initializeWithTask(task)

        if (task is SpellingTask && ::questionText.isInitialized && ::hintText.isInitialized) {
            questionText.text = "Вставьте пропущенную букву:\n${task.wordWithGap}"
            hintText.text = "Подсказка: ${task.hint}"
            answerInput.setText("")
        }
    }

    private fun submitAnswer(answer: String) {
        onAnswerSubmitted?.invoke(answer)
    }
}
