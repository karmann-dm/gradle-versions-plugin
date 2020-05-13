package com.karmanno.plugins.tasks;

import com.karmanno.plugins.services.PrepareVersionService;
import com.karmanno.plugins.utils.GitUtils;
import org.eclipse.jgit.api.Git;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class AssignVersionTask extends DefaultTask {
    @TaskAction
    public void doAction() {
        try {
            Project project = getProject();
            Git git = GitUtils.gitRepo(project);
            String version = new PrepareVersionService().getVersion(git).printVersion();
            project.setVersion(version);
        } catch (Exception ignored) {

        }
    }
}
