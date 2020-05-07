package com.karmanno.plugins.tasks;

import com.karmanno.plugins.services.PrepareVersionService;
import com.karmanno.plugins.utils.GitUtils;
import org.eclipse.jgit.api.Git;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class PrintArtifactTask extends DefaultTask {
    @TaskAction
    public void doTask() {
        Project project = getProject();
        Git git = GitUtils.gitRepo(project);
        String projectName = project.getName();
        String version = new PrepareVersionService().getVersion(git).printVersion();
        System.out.println(String.format(
                "%s:%s", projectName, version
        ));
    }
}
