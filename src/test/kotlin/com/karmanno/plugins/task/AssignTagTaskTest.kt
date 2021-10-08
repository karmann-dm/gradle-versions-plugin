package com.karmanno.plugins.task

import com.karmanno.plugins.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class AssignTagTaskTest {
    @Test
    fun `assignTag should put git tag`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)

        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        git.commit().setMessage("message").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(AssignTagTask::class.java).single().doTask()

        // then:
        val tags = git.tagList().call().toList()
        Assertions.assertThat(tags).hasSize(1)
        Assertions.assertThat(tags.single()).isNotNull
        Assertions.assertThat(tags.single().name).isEqualTo("refs/tags/0.0.1")
    }
}