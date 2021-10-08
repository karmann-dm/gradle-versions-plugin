package com.karmanno.plugins.util

import com.karmanno.plugins.version.CurrentVersion
import com.karmanno.plugins.version.VersionInfo
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.Project
import java.io.File

class GitUtils {
    companion object {
        fun repo(project: Project): Git = try {
            Git.wrap(FileRepository(gitDirectory(project.projectDir)))
        } catch (e: Exception) {
            project.logger.error("Couldn't acquire git directory for dir: ${project.projectDir}")
            throw RuntimeException(e)
        }

        private fun gitDirectory(directory: File): File {
            val gitDir = scanForGitRoot(directory)
            if (!gitDir.exists()) {
                throw IllegalArgumentException("Cannot find '.git' directory")
            }
            return gitDir
        }

        private fun scanForGitRoot(directory: File): File {
            val gitDir = File(directory, ".git")
            if (gitDir.exists()) {
                return gitDir
            }
            if (directory.parentFile == null) {
                return gitDir
            }
            return scanForGitRoot(directory.parentFile)
        }

        fun prepareVersion(git: Git): CurrentVersion {
            val branch = git.repository.branch
            val tagList = git.tagList().call()

            if (tagList.isEmpty())
                return CurrentVersion(versionInfo = VersionInfo(patch = 1, branchName = branch), branch = branch)

            val (latestTag, latestTagCommit) = calculateLatestTagCommit(tagList, git)
            val headId = git.repository.resolve(Constants.HEAD)
            val latestVersionInfo = VersionInfo.fromString(latestTag.name)

            if (latestVersionInfo.branchName == VersionInfo.DEFAULT_BRANCH) { // switch from release to snapshot
                if (branch != VersionInfo.DEFAULT_BRANCH) {
                    latestVersionInfo.apply {
                        branchName = branch
                        build = 0
                    }
                }
            } else {
                if (branch == VersionInfo.DEFAULT_BRANCH) { // switch from snapshot to release
                    latestVersionInfo.apply {
                        branchName = VersionInfo.DEFAULT_BRANCH
                        build = null
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
}