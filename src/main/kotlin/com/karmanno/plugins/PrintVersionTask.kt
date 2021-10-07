package com.karmanno.plugins

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Ref
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
            return CurrentVersion(versionInfo = VersionInfo(patch = 1, branchName = branch), branch = branch)

        val (latestTag, latestTagCommit) = calculateLatestTagCommit(tagList, git)
        val headId = git.repository.resolve(Constants.HEAD)
        val latestVersionInfo = VersionInfo.fromString(latestTag.name)

        if (latestVersionInfo.branchName == VersionInfo.DEFAULT_BRANCH) {
            if (branch != VersionInfo.DEFAULT_BRANCH) {
                latestVersionInfo.apply {
                    branchName = branch
                    build = 0
                }
            }
        }

        val commits = git.log()
            .addRange(latestTagCommit, headId)
            .call()

        return CurrentVersion(
            versionInfo = latestVersionInfo,
            commits = commits,
            branch = branch
        )
    }

    private fun calculateLatestTagCommit(tagList: List<Ref>, git: Git): Pair<Ref, RevCommit> {
        var latestTag = tagList.first()
        var latestCommitForLatestTag = calculateLatestDateOfTag(git, latestTag)

        tagList.forEach {
            val commit = calculateLatestDateOfTag(git, it)
            if (commit.authorIdent.`when`.after(latestCommitForLatestTag.authorIdent.`when`)) {
                latestTag = it
                latestCommitForLatestTag = commit
            }
        }

        return Pair(latestTag, latestCommitForLatestTag)
    }

    private fun calculateLatestDateOfTag(git: Git, ref: Ref): RevCommit {
        val log = git.log()

        val peeledRef = git.repository.refDatabase.peel(ref)
        if (peeledRef.peeledObjectId != null) {
            log.add(peeledRef.peeledObjectId)
        } else {
            log.add(ref.objectId)
        }

        val logs = log.call().sortedBy { commit -> commit.authorIdent.`when` }.toList()

        return logs.last()
    }
}

data class CurrentVersion(
    val versionInfo: VersionInfo,
    val commits: Iterable<RevCommit>? = null,
    val branch: String
)