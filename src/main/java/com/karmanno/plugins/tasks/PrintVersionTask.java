package com.karmanno.plugins.tasks;

import com.karmanno.plugins.utils.GitUtils;
import com.karmanno.plugins.domain.VersionInfo;
import com.karmanno.plugins.services.VersionsService;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import java.util.List;

public class PrintVersionTask extends DefaultTask {

    @TaskAction
    @SneakyThrows
    public void doTask() {
        Project project = getProject();
        Git git = GitUtils.gitRepo(project);

        String branchName = git.getRepository().getBranch();

        List<Ref> tagList = git.tagList().call();
        Ref latestTag = tagList.get(tagList.size() - 1);
        VersionInfo latestVersionInfo = VersionInfo.fromTagString(latestTag.getName());

        ObjectId headId = git.getRepository().resolve(Constants.HEAD);
        Iterable<RevCommit> commitsBetweenHeadAndLatestTag = git.log()
                .addRange(git.getRepository().peel(latestTag).getPeeledObjectId(), headId)
                .call();

        String version = new VersionsService()
                .calculateNewVersions(latestVersionInfo, branchName, commitsBetweenHeadAndLatestTag)
                .printVersion();
        getLogger().info(version);
    }
}
