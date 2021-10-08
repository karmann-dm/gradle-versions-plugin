package com.karmanno.plugins.task

import com.karmanno.plugins.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class PrintArtifactTaskTest {

    @Test
    fun `printArtifact task when task is called then print artifact`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintArtifactTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("test-name:0.0.1")
    }
}