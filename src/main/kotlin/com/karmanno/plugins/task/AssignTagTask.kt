package com.karmanno.plugins.task

import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.revwalk.RevWalk
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class AssignTagTask: DefaultTask() {

    @Input
    @Optional
    @Option(description = "Release branch, if it is different to master", option = "releaseBranch")
    var releaseBranch: String? = null


    @TaskAction
    fun doTask() {
        val (git, nextVersion) = GenericIncreaseVersionTask().calculateVersionString(project, releaseBranch)

        val head = RevWalk(git.repository).parseCommit(
            git.repository.resolve(Constants.HEAD)
        )

        git.tag()
            .setObjectId(head)
            .setName(nextVersion)
            .setTagger(head.authorIdent)
            .call()
    }
}