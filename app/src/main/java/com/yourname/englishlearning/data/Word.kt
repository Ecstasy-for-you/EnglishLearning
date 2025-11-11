package com.yourname.englishlearning.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class TaskType {
    ALL, YES_NO, SPELLING, MATCHING
}
//контейнер для слов
data class WordData(
    @SerializedName("words") val words: List<Word>
) : Serializable

//модель слова
data class Word(
    val id: Int,
    val english: String,
    val russian: String,
    val category: String
) : Serializable

//базовый класс заданий
sealed class Task : Serializable {
    abstract val id: Int
    abstract val correctAnswer: String
    abstract val englishWord: String
}

data class YesNoTask(
    override val id: Int,
    override val englishWord: String,
    val russianTranslation: String,
    val isCorrect: Boolean
) : Task(), Serializable {
    override val correctAnswer: String get() = if (isCorrect) "да" else "нет"
}

data class SpellingTask(
    override val id: Int,
    override val englishWord: String,
    val wordWithGap: String,
    val correctLetter: String,
    val hint: String
) : Task(), Serializable {
    override val correctAnswer: String = correctLetter
}

data class MatchingTask(
    override val id: Int,
    override val englishWord: String,
    override val correctAnswer: String,
    val options: List<String>
) : Task(), Serializable
