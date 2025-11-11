package com.yourname.englishlearning.ui.results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yourname.englishlearning.R
import com.yourname.englishlearning.data.SpellingTask
import com.yourname.englishlearning.data.YesNoTask
import com.yourname.englishlearning.data.MatchingTask

//отображает список результатов тестирования
class ResultsAdapter(private val results: List<ResultItem>) :
    RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val userAnswerText: TextView = itemView.findViewById(R.id.userAnswerText)
        val correctAnswerText: TextView = itemView.findViewById(R.id.correctAnswerText)
        val statusText: TextView = itemView.findViewById(R.id.statusText)
    }
//создание элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view)
    }
//наполнение данными
    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]

        // Устанавливаем вопрос в зависимости от типа задания
        when (val task = result.task) {
            is YesNoTask -> {
                holder.questionText.text = "Правильный ли перевод?\n${task.englishWord} - ${task.russianTranslation}"

                val expectedAnswer = if (task.isCorrect) "да" else "нет"
                holder.correctAnswerText.text = "Правильный ответ: $expectedAnswer"
            }
            is SpellingTask -> {
                holder.questionText.text = "Вставьте букву:\n${task.wordWithGap}"
                holder.correctAnswerText.text = "Правильный ответ: ${task.correctLetter} (${task.hint})"
            }
            is MatchingTask -> {
                holder.questionText.text = "Выберите перевод для:\n${task.englishWord}"
                holder.correctAnswerText.text = "Правильный ответ: ${task.correctAnswer}"
            }
            else -> {
                holder.questionText.text = "Вопрос: ${task.englishWord}"
                holder.correctAnswerText.text = "Правильный ответ: ${task.correctAnswer}"
            }
        }

        // Устанавливаем ответ пользователя
        holder.userAnswerText.text = "Ваш ответ: ${result.userAnswer}"

        // Устанавливаем статус (правильно/неправильно)
        if (result.isCorrect) {
            holder.statusText.text = "✓ Правильно"
            holder.statusText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            holder.userAnswerText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        } else {
            holder.statusText.text = "✗ Неправильно"
            holder.statusText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.userAnswerText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
    }
//количество элементов
    override fun getItemCount(): Int = results.size
}
