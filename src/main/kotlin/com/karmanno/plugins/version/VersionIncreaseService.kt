package com.karmanno.plugins.version

import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.Project

class VersionIncreaseService {
    companion object {
        const val HEADER_DELIMETER = ":"
    }

    fun calculateNext(project: Project, currentVersion: CurrentVersion,): VersionInfo {
        val versionToIncrease = currentVersion.versionInfo
        val strategy = extractStrategyFromCommits(
            currentVersion.commits?.toList() ?: emptyList(),
            project, currentVersion.branch
        )
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

        if (strategy is NoIncreaseStrategy) {
            project.logger.warn("Warning: no version increase strategy found. Version remain untouched.")
        }

        return strategy
    }

    private fun isRelease(branch: String) = VersionInfo.DEFAULT_BRANCH == branch

    private fun extractIncreaseStrategy(prefix: String, release: Boolean) =
        (if (release) IncreaseStrategy.release() else IncreaseStrategy.snapshot()).first {
            it.supportablePatterns().contains(prefix.toLowerCase())
        }

    private fun prefixInvalid(prefix: List<String>) = prefix.size < 2
}