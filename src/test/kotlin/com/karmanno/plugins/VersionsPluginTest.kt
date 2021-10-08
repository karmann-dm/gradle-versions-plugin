package com.karmanno.plugins

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class VersionsPluginTest {
    @Test
    fun `using plugin id should apply successfully`(@TempDir tempDir: File) {
        // given:
        val project = TestUtils.initializeGradleProject(tempDir)

        // when:
        project.pluginManager.apply("com.karmanno.plugins.semver")

        // then:
        Assertions.assertThat(project.plugins.getPlugin(VersionsPlugin::class.java)).isNotNull
        Assertions.assertThat(project.tasks.findByName("printVersion")).isNotNull
        Assertions.assertThat(project.tasks.findByName("printArtifact")).isNotNull
    }
}