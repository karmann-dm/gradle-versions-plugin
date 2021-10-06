package com.karmanno.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionsPlugin: Plugin<Project> {
    companion object {
        const val PRINT_VERSION = "printVersion"
    }

    override fun apply(target: Project) {
        target.tasks.register(PRINT_VERSION, PrintVersionTask::class.java)
    }
}