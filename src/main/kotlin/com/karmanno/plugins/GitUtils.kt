package com.karmanno.plugins

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
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
    }
}