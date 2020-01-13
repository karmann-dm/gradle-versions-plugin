package com.karmanno.plugins;

import org.gradle.internal.impldep.org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public class VersionsService {
    public List<VersionIncreaseHandler> handlers = List.of(
            new MajorIncreaseHandler(),
            new MinorIncreaseHandler(),
            new PatchIncreaseHandler(),
            new BuildIncreaseHandler()
    );

    public VersionInfo calculateNewVersions(VersionInfo previousVersionInfo, Iterable<RevCommit> commits) {
        String maxPriorityPrefix = "";

        for (RevCommit commit : commits) {
            String fullMessage = commit.getFullMessage();
            String[] splittedForPrefix = fullMessage.split(":");
            if (splittedForPrefix.length < 2) {
                continue;
            }
            String prefix = splittedForPrefix[0].trim();
            maxPriorityPrefix = prefix;
        }

        String finalMaxPriorityPrefix = maxPriorityPrefix;
        VersionIncreaseHandler increaseHandler = handlers.stream()
                .filter(h -> h.getSupportablePatterns().contains(finalMaxPriorityPrefix))
                .findAny()
                .orElse(new NoIncreaseHandler());

        return increaseHandler.handle(previousVersionInfo);
    }
}
