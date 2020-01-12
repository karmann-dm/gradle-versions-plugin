package com.karmanno.plugins;

import groovy.lang.Closure;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git;
import org.gradle.internal.impldep.org.eclipse.jgit.lib.Constants;
import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectId;
import org.gradle.internal.impldep.org.eclipse.jgit.lib.Ref;
import org.gradle.internal.impldep.org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

/**
 *     prefix format:
 *        PREFIX: COMMIT MESSAGE
 *     prefixes:
 *        WIP/wip - increase build version
 *        fix/FIX - increase patch version
 *        feat/FEAT/feature - increase minor version
 *        global/GLOBAL - increase major version
 */
public class VersionsPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        Git git = GitUtils.gitRepo(project);

        project.getExtensions().getExtraProperties().set("versionInfo",
                new Closure<VersionInfo>(this, this) {
            @Override
            public VersionInfo call(Object arguments) {
                try {
                    List<Ref> tagList = git.tagList().call();
                    Ref latestTag = tagList.get(tagList.size() - 1);
                    VersionInfo latestVersionInfo = VersionInfo.fromTagString(latestTag.getName());

                    ObjectId latestTagId = latestTag.getObjectId();
                    ObjectId headId = git.getRepository().resolve(Constants.HEAD);
                    Iterable<RevCommit> commitsBetweenHeadAndLatestTag = git.log().addRange(latestTagId, headId).call();

                    return VersionsService.calculateNewVersions(latestVersionInfo, commitsBetweenHeadAndLatestTag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new VersionInfo();
            }
        });
    }
}
