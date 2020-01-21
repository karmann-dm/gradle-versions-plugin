package com.karmanno.plugins;

import com.karmanno.plugins.tasks.PrintVersionTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class VersionsPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().register("printVersion", PrintVersionTask.class);
    }
}
