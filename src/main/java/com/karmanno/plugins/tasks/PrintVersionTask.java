package com.karmanno.plugins.tasks;

import com.karmanno.plugins.utils.GitUtils;
import com.karmanno.plugins.domain.VersionInfo;
import com.karmanno.plugins.services.VersionsService;
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
    public void doTask() {
        Project project = getProject();
        Git git = GitUtils.gitRepo(project);

        try {
            String branchName = git.getRepository().getBranch();

            List<Ref> tagList = git.tagList().call();
            Ref latestTag = tagList.get(tagList.size() - 1);
            VersionInfo latestVersionInfo = VersionInfo.fromTagString(latestTag.getName());

            ObjectId latestTagId = latestTag.getObjectId();
            ObjectId headId = git.getRepository().resolve(Constants.HEAD);
            Iterable<RevCommit> commitsBetweenHeadAndLatestTag = git.log().addRange(latestTagId, headId).call();

            String version = new VersionsService()
                    .calculateNewVersions(latestVersionInfo, branchName, commitsBetweenHeadAndLatestTag)
                    .printVersion();
            getLogger().info(version);
        } catch (Exception e) {
            String version = new VersionInfo()
                    .printVersion();
            getLogger().info(version);
        }
    }
}
