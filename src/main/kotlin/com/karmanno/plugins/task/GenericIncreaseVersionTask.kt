package com.karmanno.plugins.task

import com.karmanno.plugins.util.GitUtils
import com.karmanno.plugins.version.VersionIncreaseService
import com.karmanno.plugins.version.VersionInfo
import org.eclipse.jgit.api.Git
import org.gradle.api.Project

class GenericIncreaseVersionTask {
    private val versionIncreaseService: VersionIncreaseService = VersionIncreaseService()

    fun calculateVersionString(project: Project, releaseBranch: String?): Pair<Git, String> {
        if (releaseBranch == null)
            VersionInfo.DEFAULT_BRANCH = "master"

        releaseBranch?.let {
            project.logger.info("Different release branch is specified: $it")
            VersionInfo.DEFAULT_BRANCH = it
        }

        val git = GitUtils.repo(project)
        val currentVersion = GitUtils.prepareVersion(git)
        return Pair(
            git,
            versionIncreaseService.calculateNext(project, currentVersion).toString()
        )
    }
}