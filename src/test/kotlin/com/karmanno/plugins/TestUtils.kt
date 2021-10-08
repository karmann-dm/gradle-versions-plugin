package com.karmanno.plugins

import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import java.io.File

class TestUtils {
    companion object {
        fun initializeGradleProject(directory: File): Project = ProjectBuilder.builder()
            .withProjectDir(directory)
            .withName("test-name")
            .build()

        fun initializeGitRepository(directory: File) = Git.init().setDirectory(directory).call()
    }
}