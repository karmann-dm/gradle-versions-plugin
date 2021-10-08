package com.karmanno.plugins.task

import com.karmanno.plugins.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class PrintVersionTaskTest {
    @Test
    fun `printVersion task when git repo is empty then should print default version`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("0.0.1")
    }

    @Test
    fun `printVersion task when git repo contains custom tags then should print default version`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("someTag").setObjectId(rev).call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("0.0.1")
    }

    @Test
    fun `printVersion task when git repo doesn't exist then should throw exception`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")

        // then:
        Assertions.assertThatThrownBy {
            project.tasks.withType(PrintVersionTask::class.java).single().doTask()
        }.hasMessageContaining("Cannot find '.git' directory")
    }

    @Test
    fun `printVersion task release major`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7").setObjectId(rev).call()
        git.commit().setMessage("some other commits out of format").setAuthor("name", "e@mail").call()
        git.commit().setMessage("global: some global changes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("2.0.0")
    }

    @Test
    fun `printVersion when different release branch is specified then print release`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.checkout().setCreateBranch(true).setName("otherBranch").call()


        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        val task: PrintVersionTask = project.tasks.withType(PrintVersionTask::class.java).single()
        task.apply {
            releaseBranch = "otherBranch"
        }
        task.doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("0.0.1")
    }

    @Test
    fun `printVersion task release minor`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7").setObjectId(rev).call()
        git.commit().setMessage("fix: skipped changes due to low priority").setAuthor("name", "e@mail").call()
        git.commit().setMessage("feat: some minor changes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("1.3.0")
    }

    @Test
    fun `printVersion task release patch`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7").setObjectId(rev).call()
        git.commit().setMessage("build: skipped changes due to low priority").setAuthor("name", "e@mail").call()
        git.commit().setMessage("fix: some fix changes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("1.2.8")
    }

    @Test
    fun `printVersion task snapshot build when switch from release branch`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7").setObjectId(rev).call()

        git.checkout().setCreateBranch(true).setName("otherBranch").call()
        val newSecondFile = File(tempDir, "newFile1.txt")
        newSecondFile.writeText("some text")
        git.add().addFilepattern("*").call()
        git.commit().setMessage("build: some build changes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("1.2.7.otherBranch.1")
    }

    @Test
    fun `printVersion task snapshot build when previous tag was specified then increase build`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7").setObjectId(rev).call()

        Thread.sleep(2000)
        git.checkout().setCreateBranch(true).setName("otherBranch").call()
        val newSecondFile = File(tempDir, "newFile1.txt")
        newSecondFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev2 = git.commit().setMessage("build: some build changes").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7.otherBranch.1").setObjectId(rev2).call()
        val newThirdFile = File(tempDir, "newFile2.txt")
        newThirdFile.writeText("some text")
        git.add().addFilepattern("*").call()
        git.commit().setMessage("build: some build changes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("1.2.7.otherBranch.2")
    }

    @Test
    fun `printVersion task snapshot build when switch from other branch to release`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev = git.commit().setMessage("message").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7").setObjectId(rev).call()

        git.checkout().setCreateBranch(true).setName("otherBranch").call()
        val newSecondFile = File(tempDir, "newFile1.txt")
        newSecondFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev2 = git.commit().setMessage("build: some build changes").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7.otherBranch").setObjectId(rev2).call()
        val newThirdFile = File(tempDir, "newFile2.txt")
        newThirdFile.writeText("some text")
        git.add().addFilepattern("*").call()
        val rev3 = git.commit().setMessage("build: some build changes").setAuthor("name", "e@mail").call()
        git.tag().setName("1.2.7.otherBranch.1").setObjectId(rev3).call()
        git.checkout().setName("master").call()

        val newFourthFile = File(tempDir, "newFile3.txt")
        newFourthFile.writeText("some text")
        git.add().addFilepattern("*").call()
        git.commit().setMessage("fix: some fix changes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("1.2.8")
    }

    @Test
    fun `increase version after 9 suffix`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)
        val git = TestUtils.initializeGitRepository(tempDir)
        val stdout = ByteArrayOutputStream()
        System.setOut(PrintStream(stdout))

        (1 until 20).forEach {
            val file = File(tempDir, "$it.txt")
            file.writeText("some text")
            git.add().addFilepattern("*").call()
            val rev = git.commit().setMessage("fix: some fix changes").setAuthor("name", "e@mail").call()
            git.tag().setName("0.0.$it").setObjectId(rev).call()
            Thread.sleep(1000)
        }

        val newFile = File(tempDir, "newFile.txt")
        newFile.writeText("some text")
        git.add().addFilepattern("*").call()
        git.commit().setMessage("fix: new fixes").setAuthor("name", "e@mail").call()

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")
        project.tasks.withType(PrintVersionTask::class.java).single().doTask()

        // then:
        Assertions.assertThat(stdout.toString().trim()).isEqualTo("0.0.20")
    }
}
