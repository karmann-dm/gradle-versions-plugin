package com.karmanno.plugins.services;

import com.karmanno.plugins.domain.VersionInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class VersionsServiceTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Git git;

    private VersionsService versionsService = new VersionsService();

    @Before
    public void setUp() throws GitAPIException {
        git = Git.init().setDirectory( tempFolder.getRoot() ).call();
    }

    @After
    public void tearDown() {
        git.getRepository().close();
    }

    @Test
    public void correctMajorVersionRelease() throws Exception {
        VersionInfo newVersion = versionsService.calculateNewVersions(
                null,
                git.getRepository().getBranch(),
                prepareWithPriority("global", "fix"));

        assertEquals("1.0.0", newVersion.printVersion());
    }

    @Test
    public void correctMinorVersionRelease() throws Exception {
        VersionInfo newVersion = versionsService.calculateNewVersions(
                null,
                git.getRepository().getBranch(),
                prepareWithPriority("feature", "fix")
        );

        assertEquals("0.1.0", newVersion.printVersion());
    }

    @Test
    public void correctPatchVersionRelease() throws Exception {
        VersionInfo newVersion = versionsService.calculateNewVersions(null,
                git.getRepository().getBranch(),
                prepareWithPriority("fix", "fix")
        );

        assertEquals("0.0.2", newVersion.printVersion());
    }

    @Test
    public void correctVersionSnapshot() throws GitAPIException, IOException {
        tempFolder.newFile("init.txt");
        git.add().addFilepattern("*").call();
        git.commit().setMessage("fix: new file").call();
        git.branchCreate().setName("dev").call();
        git.checkout().setName("dev").call();

        VersionInfo newVersion = versionsService.calculateNewVersions(null,
                git.getRepository().getBranch(),
                prepareWithPriority("global", "fix")
        );

        assertEquals("0.0.1.dev.2", newVersion.printVersion());
    }

    private List<RevCommit> prepareWithPriority(String priority, String others) throws IOException, GitAPIException {
        tempFolder.newFile("File_1.txt");
        git.add().addFilepattern("*").call();
        RevCommit initial = git.commit().setMessage(others + ": new file").call();
        tempFolder.newFile("File_2.txt");
        RevCommit second = git.commit().setMessage(priority + ": second file").call();
        tempFolder.newFile("File_3.txt");
        RevCommit third = git.commit().setMessage(others + ": third file").call();

        return List.of(initial, second, third);
    }
}
