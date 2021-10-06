package com.karmanno.plugins

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class PrintVersionTask: DefaultTask() {
    private val versionIncreaseService: VersionIncreaseService = VersionIncreaseService()

    @TaskAction
    fun doTask() {
        val project = project
        val git = GitUtils.repo(project)

        val currentVersion = prepareVersion(git)
        println(versionIncreaseService.calculateNext(project, currentVersion).toString())
    }

    private fun prepareVersion(git: Git): CurrentVersion {
        val branch = git.repository.branch
        val tagList = git.tagList().call()

        if (tagList.isEmpty())
            return CurrentVersion(versionInfo = VersionInfo(branchName = branch), branch = branch)

        val latestTag = tagList.last()
        val latestVersionInfo = VersionInfo.fromString(latestTag.name)

        val headId = git.repository.resolve(Constants.HEAD)
        val commits = git.log()
            .addRange(git.repository.refDatabase.peel(latestTag).peeledObjectId, headId)
            .call()

        return CurrentVersion(
            versionInfo = latestVersionInfo,
            commits = commits,
            branch = branch
        )
    }
}

data class CurrentVersion(
    val versionInfo: VersionInfo,
    val commits: Iterable<RevCommit>? = null,
    val branch: String
)