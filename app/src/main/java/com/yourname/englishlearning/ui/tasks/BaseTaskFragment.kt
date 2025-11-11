package com.yourname.englishlearning.ui.tasks

import androidx.fragment.app.Fragment
import com.yourname.englishlearning.data.Task

abstract class BaseTaskFragment : Fragment() {

    // хранится текущее задание
    protected var currentTask: Task? = null

//инициализация задания
    open fun initializeWithTask(task: Task) {
        this.currentTask = task
    }

//Callback для обработки ответов
    var onAnswerSubmitted: ((String) -> Unit)? = null

}
