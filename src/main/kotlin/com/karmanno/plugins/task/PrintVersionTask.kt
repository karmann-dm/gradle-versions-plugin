package com.karmanno.plugins.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class PrintVersionTask: DefaultTask() {
    @Input
    @Optional
    @Option(description = "Release branch, if it is different to master", option = "releaseBranch")
    var releaseBranch: String? = null

    @TaskAction
    fun doTask() {
        println(GenericIncreaseVersionTask().calculateVersionString(project, releaseBranch).second)
    }
}