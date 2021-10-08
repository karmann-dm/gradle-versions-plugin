package com.karmanno.plugins

import com.karmanno.plugins.task.AssignTagTask
import com.karmanno.plugins.task.PrintArtifactTask
import com.karmanno.plugins.task.PrintVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionsPlugin: Plugin<Project> {
    companion object {
        const val PRINT_VERSION = "printVersion"
        const val PRINT_ARTIFACT = "printArtifact"
        const val ASSIGN_TAG = "assignTag"
    }

    override fun apply(target: Project) {
        target.tasks.register(PRINT_VERSION, PrintVersionTask::class.java)
        target.tasks.register(PRINT_ARTIFACT, PrintArtifactTask::class.java)
        target.tasks.register(ASSIGN_TAG, AssignTagTask::class.java)
    }
}