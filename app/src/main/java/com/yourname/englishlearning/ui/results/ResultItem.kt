package com.yourname.englishlearning.ui.results

import com.yourname.englishlearning.data.Task
import java.io.Serializable

data class ResultItem(
    val task: Task,
    val userAnswer: String,
    val isCorrect: Boolean
) : Serializable
