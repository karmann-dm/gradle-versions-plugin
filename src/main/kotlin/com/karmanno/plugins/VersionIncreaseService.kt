package com.karmanno.plugins

import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.Project

class VersionIncreaseService {
    companion object {
        const val HEADER_DELIMETER = ":"
    }

    fun calculateNext(project: Project, currentVersion: CurrentVersion,): VersionInfo {
        val versionToIncrease = if (currentVersion.commits == null) {
            return VersionInfo(branchName = currentVersion.branch)
        } else currentVersion.versionInfo

        val strategy = extractStrategyFromCommits(currentVersion.commits.toList(), project, currentVersion.branch)
        return strategy.increase(versionToIncrease)
    }

    private fun extractStrategyFromCommits(commits: List<RevCommit>, project: Project, branch: String): IncreaseStrategy {
        var strategy: IncreaseStrategy = NoIncreaseStrategy()

        for (commit: RevCommit in commits) {
            val fullCommitMessage = commit.fullMessage

            project.logger.debug("Fetch commit: $fullCommitMessage")

            val splitForPrefix = fullCommitMessage.split(HEADER_DELIMETER)
            if (prefixInvalid(splitForPrefix)) {
                project.logger.debug("Error: commit $fullCommitMessage doesn't satisfy requirements")
                continue
            }

            val prefix = splitForPrefix[0].trim()
            val extractedStrategy = extractIncreaseStrategy(prefix, isRelease(branch))
            val extractedPriority = extractedStrategy.supportablePriority()

            if (extractedPriority.value >= strategy.supportablePriority().value) {
                strategy = extractedStrategy
            }
        }

        return strategy
    }

    private fun isRelease(branch: String) = VersionInfo.DEFAULT_BRANCH == branch

    private fun extractIncreaseStrategy(prefix: String, release: Boolean) =
        ( if(release) IncreaseStrategy.release() else IncreaseStrategy.snapshot() )
            .filter { it.supportablePatterns().contains(prefix.toLowerCase()) }
            .first()

    private fun prefixInvalid(prefix: List<String>) = prefix.size < 2
}