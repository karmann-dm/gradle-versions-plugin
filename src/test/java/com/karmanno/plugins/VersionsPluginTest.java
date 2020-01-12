package com.karmanno.plugins;

import lombok.SneakyThrows;
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git;
import org.gradle.internal.impldep.org.eclipse.jgit.api.errors.GitAPIException;
import org.gradle.internal.impldep.org.eclipse.jgit.lib.Ref;
import org.gradle.internal.impldep.org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class VersionsPluginTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Git git;

    @Before
    public void setUp() throws GitAPIException {
        git = Git.init().setDirectory( tempFolder.getRoot() ).call();
    }

    @After
    public void tearDown() {
        git.getRepository().close();
    }

    @Test
    public void testReleaseWithMajor() throws Exception {
        RevCommit firstCommit = commitFile("file1.txt", "Content 1", "global: some");
        Ref tag = tagCommit(firstCommit, "1.0.0");
    }

    @Test
    public void testReleaseWithMinor() {

    }

    @Test
    public void testReleaseWithPatch() {

    }

    @Test
    public void testReleaseWithBranch() {

    }

    @Test
    public void testSnapshotWithMajor() {

    }

    @Test
    public void testSnapshotWithMinor() {

    }

    @Test
    public void testSnapshotWithPatch() {

    }

    @Test
    public void testSnapshotWithBranch() {

    }

    @Test
    public void testSnapshotWithBuild() {

    }

    @SneakyThrows
    private Ref tagCommit(RevCommit commit, String name) {
        return git.tag().setObjectId(commit).setName(name).call();
    }

    @SneakyThrows
    private RevCommit commitFile(String name, String content, String message) {
        writeFile(name, content);
        git.add().addFilepattern(name).call();
        return git.commit().setMessage(message).call();
    }

    @SneakyThrows
    private File writeFile(String name, String content) {
        File file = new File(git.getRepository().getWorkTree(), name);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        }
        return file;
    }
}
