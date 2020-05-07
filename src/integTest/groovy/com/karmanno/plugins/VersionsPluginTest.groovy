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
    def "should print release"() {
        given:

        git = Git.init().setDirectory(getProjectDir()).call()
        git.commit().setMessage("fix: new something").call()
        git.commit().setMessage("fix: something new").call()
        def third = git.commit().setMessage("feat: wtf whats this").call()
        git.tag().setName("1.2.3").setObjectId(third).call()
        git.commit().setMessage("fix: something else").call()
        git.commit().setMessage("feat: whaaat").call()

        settingsFile << "rootProject.name = 'some-project'"
        buildFile << "${applyPlugin(VersionsPlugin)}\n" +
                "apply plugin: 'java'"

        when:
        def result = runTasks("printVersion")
        git.close()

        then:
        result.wasExecuted("printVersion")
        result.success
        result.standardOutput.contains("1.3.0")
    }

    @Test
    def "should print snapshot"() {
        given:

        git = Git.init().setDirectory(getProjectDir()).call()
        git.commit().setMessage("fix: new something").call()
        git.branchCreate().setName("dev").call()
        git.checkout().setName("dev").call()
        git.commit().setMessage("fix: something new").call()
        def third = git.commit().setMessage("feat: wtf whats this").call()
        git.tag().setName("1.2.3").setObjectId(third).call()
        git.commit().setMessage("fix: something else").call()
        git.commit().setMessage("global: whaaat").call()

        settingsFile << "rootProject.name = 'some-project'"
        buildFile << "${applyPlugin(VersionsPlugin)}\n" +
                "apply plugin: 'java'"

        when:
        def result = runTasks("printVersion")
        git.close()

        then:
        result.wasExecuted("printVersion")
        result.success
        result.standardOutput.contains("1.2.3.dev.1")
    }

    @Test
    def 'should print initial'() {
        given:

        git = Git.init().setDirectory(getProjectDir()).call()
        git.commit().setMessage("Initial").call()

        settingsFile << "rootProject.name = 'some-project'"
        buildFile << "${applyPlugin(VersionsPlugin)}\n" +
                "apply plugin: 'java'"

        when:
        def result = runTasks("printVersion")
        git.close()

        then:
        result.wasExecuted("printVersion")
        result.success
        result.standardOutput.contains("0.0.1")
    }

    @Test
    def 'should assign task after build'() {
        given:
        git = Git.init().setDirectory(getProjectDir()).call()
        settingsFile << "rootProject.name = 'some-project'"
        buildFile << "${applyPlugin(VersionsPlugin)}\n" +
                "apply plugin: 'java'"

        when:
        def result = runTasks("build")

        then:
        result.wasExecuted("assignVersion")
        result.success
    }

    @Test
    def 'should put tag'() {
        given:
        git = Git.init().setDirectory(getProjectDir()).call()
        git.commit().setMessage("Initial").call()
        settingsFile << "rootProject.name = 'some-project'"
        buildFile << "${applyPlugin(VersionsPlugin)}\n" +
                "apply plugin: 'java'"

        when:
        def result = runTasks("assignTag")
        def tagList = git.tagList().call()

        then:
        result.success
        result.wasExecuted("assignTag")
        tagList.size() == 1
        tagList.get(0).name.contains("0.0.1")
    }

    @Test
    def 'should print artifact'() {
        given:
        git = Git.init().setDirectory(getProjectDir()).call()
        settingsFile << "rootProject.name = 'some-project'"
        buildFile << "${applyPlugin(VersionsPlugin)}\n" +
                "apply plugin: 'java'"

        when:
        def result = runTasks("printArtifact")

        then:
        result.standardOutput.contains("some-project:0.0.1")
        result.wasExecuted("printArtifact")
        result.success
    }
}
