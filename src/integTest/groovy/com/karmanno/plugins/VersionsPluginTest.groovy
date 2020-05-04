package com.karmanno.plugins

import nebula.test.IntegrationSpec
import org.eclipse.jgit.api.Git
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class VersionsPluginTest extends IntegrationSpec {
    private Git git

    @Rule
    private TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Test
    def "should print snapshot"() {
        given:
        setProjectDir(temporaryFolder.getRoot())
        settingsFile << "rootProject.name = 'some-project'"
        buildFile << applyPlugin(VersionsPlugin)

        when:
        def result = runTasks("printVersion")

        then:
        result.wasExecuted("printVersion")
        result.success
    }
}
