package com.yourname.englishlearning.data

import android.content.Context
import com.google.gson.Gson
//генерирует все типы заданий из слов, загруженных из JSON файла
class TaskRepository(private val context: Context) {

    private var wordData: WordData? = null

    private fun loadWords(): WordData {
        if (wordData == null) {
            val jsonString = context.assets.open("words.json")
                .bufferedReader().use { it.readText() }
            wordData = Gson().fromJson(jsonString, WordData::class.java)
        }
        return wordData!!
    }

    fun generateYesNoTasks(): List<YesNoTask> {
        val words = loadWords().words.shuffled()
        return words.take(10).mapIndexed { index, word ->
            val isCorrect = index % 4 != 0// правильность переводов (75% правильных, 25% неправильных
            val displayedTranslation = if (isCorrect) {
                word.russian
            } else {
                // Берем случайный неправильный перевод из той же категории
                words.filter {
                    it.category == word.category && it.id != word.id
                }.random().russian
            }

            YesNoTask(
                id = word.id * 10 + 1,
                englishWord = word.english,
                russianTranslation = displayedTranslation,
                isCorrect = isCorrect
            )
        }
    }

    fun generateSpellingTasks(): List<SpellingTask> {
        val words = loadWords().words
            .filter { it.english.length in 5..8 }
            .shuffled()

        return words.map { word ->
            val randomIndex = (1 until word.english.length - 1).random()
            val wordWithGap = StringBuilder(word.english).apply {
                setCharAt(randomIndex, '_')
            }.toString()

            SpellingTask(
                id = word.id * 10 + 2,
                englishWord = word.english,
                wordWithGap = wordWithGap,
                correctLetter = word.english[randomIndex].toString(),
                hint = word.russian
            )
        }
    }

    fun generateMatchingTasks(): List<MatchingTask> {
        val words = loadWords().words.shuffled()

        return words.map { word ->
            val wrongOptions = loadWords().words
                .filter {
                    it.category == word.category &&
//                            Исключаем текущее слово
                            it.id != word.id
                }
                .shuffled()
                .take(2)
                .map { it.russian }

            val allOptions = (wrongOptions + word.russian).shuffled()

            MatchingTask(
                id = word.id * 10 + 3,
                englishWord = word.english,
                correctAnswer = word.russian,
                options = allOptions
            )
        }
    }

    fun getAllTasks(): List<Task> {
        return generateYesNoTasks().shuffled().take(3) +
                generateSpellingTasks().shuffled().take(3) +
                generateMatchingTasks().shuffled().take(3)
    }
}
