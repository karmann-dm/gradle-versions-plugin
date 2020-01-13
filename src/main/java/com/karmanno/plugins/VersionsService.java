package com.karmanno.plugins;

import org.gradle.internal.impldep.org.eclipse.jgit.revwalk.RevCommit;

public class VersionsService {
    public static VersionInfo calculateNewVersions(VersionInfo previousVersionInfo, Iterable<RevCommit> commits) {
        for (RevCommit commit : commits) {

        }
    }
}
