package com.yourname.englishlearning.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.Task
import com.yourname.englishlearning.data.YesNoTask

class YesNoTaskFragment : BaseTaskFragment() {

    private lateinit var questionText: TextView
    private lateinit var yesButton: Button
    private lateinit var noButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_yes_no, container, false)

        questionText = view.findViewById(R.id.questionText)
        yesButton = view.findViewById(R.id.yesButton)
        noButton = view.findViewById(R.id.noButton)

        yesButton.setOnClickListener { submitAnswer("да") }
        noButton.setOnClickListener { submitAnswer("нет") }

        // Загружаем задание из аргументов
        arguments?.getSerializable("task")?.let { task ->
            if (task is YesNoTask) {
                initializeWithTask(task)
            }
        }

        return view
    }

    override fun initializeWithTask(task: Task) {
        super.initializeWithTask(task)

        if (task is YesNoTask && ::questionText.isInitialized) {
            questionText.text = "Правильный ли перевод?\n${task.englishWord} - ${task.russianTranslation}"
        }
    }

    private fun submitAnswer(answer: String) {
        onAnswerSubmitted?.invoke(answer)
    }
}
