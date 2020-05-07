package com.karmanno.plugins

import com.karmanno.plugins.tasks.AssignTagTask
import com.karmanno.plugins.tasks.AssignVersionTask
import com.karmanno.plugins.tasks.PrintArtifactTask
import com.karmanno.plugins.tasks.PrintVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionsPlugin implements Plugin<Project> {
    public static final String PRINT_VERSION_TASK = "printVersion"
    public static final String ASSIGN_TAG_TASK = "assignTag"
    public static final String ASSIGN_VERSION_TASK = "assignVersion"
    public static final String PRINT_ARTIFACT_TASK = "printArtifact"
    public static final String BUILD_TASK = "build"

    @Override
    void apply(Project project) {
        project.tasks.register(PRINT_VERSION_TASK, PrintVersionTask)
        project.tasks.register(PRINT_ARTIFACT_TASK, PrintArtifactTask)
        project.tasks.register(ASSIGN_TAG_TASK, AssignTagTask)

        project.afterEvaluate {
            def assignVersionTask = project.tasks.register(ASSIGN_VERSION_TASK, AssignVersionTask)
            project.tasks.findByName(BUILD_TASK).with {
                dependsOn assignVersionTask
            }
        }
    }
}
