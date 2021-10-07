package com.karmanno.plugins

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class GitUtilsTest {

    @Test
    fun `repo when git repository doesnt exist then throw exception`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)

        // when/then:
        Assertions.assertThatThrownBy { GitUtils.repo(project) }
            .hasMessageContaining("Cannot find '.git' directory")
            .hasCauseInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `repo when git repository exists then return instance`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        TestUtils.initializeGitRepository(tempDir)

        // when:
        val git = GitUtils.repo(project)

        // then:
        Assertions.assertThat(File(tempDir, ".git").exists())
        Assertions.assertThat(git).isNotNull
        Assertions.assertThat(git.repository).isNotNull
    }
}