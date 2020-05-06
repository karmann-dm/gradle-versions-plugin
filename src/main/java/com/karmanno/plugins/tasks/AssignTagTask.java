package com.karmanno.plugins.tasks;

import com.karmanno.plugins.services.PrepareVersionService;
import com.karmanno.plugins.utils.GitUtils;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class AssignTagTask extends DefaultTask {

    @TaskAction
    @SneakyThrows
    public void doTask() {
        Project project = getProject();
        Git git = GitUtils.gitRepo(project);
        String version = new PrepareVersionService().getVersion(git).printVersion();
        project.setVersion(version);
        git.tag().setObjectId(
                new RevWalk(git.getRepository()).parseCommit(
                        git.getRepository().resolve(Constants.HEAD)
                )
        ).setName(version).call();
    }
}
