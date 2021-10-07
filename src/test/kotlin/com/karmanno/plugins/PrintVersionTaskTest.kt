package com.karmanno.plugins

import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

class PrintVersionTaskTest: WordSpec({

    "Using the Plugin ID" should {
        "Apply the plugin" { withProject {
            it.plugins.getPlugin(VersionsPlugin::class.java) shouldNotBe null
        } }
    }

    "Using the printVersion task" should {
        "Run print task on non-git repo" { withProject { project ->
            project.tasks.withType(PrintVersionTask::class.java).single().doTask()
        } }
    }
})

private fun withProject(test: (Project) -> Unit) {
    val project = project()
    test(project)
}

private fun project() = ProjectBuilder.builder().build().also { project ->
    project.pluginManager.apply("com.karmanno.plugins.semver")
}