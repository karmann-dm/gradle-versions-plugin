package com.karmanno.plugins.services;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;
import com.karmanno.plugins.handlers.VersionIncreaseHandler;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public class VersionsService {
    private static final String HEADER_DELIMETER = ":";
    private static final String RELEASE_BRANCH = "master";

    public VersionInfo calculateNewVersions(VersionInfo previousVersionInfo,
                                     String branch,
                                     Iterable<RevCommit> commits) {
        if (previousVersionInfo == null) { // new version
            previousVersionInfo = new VersionInfo()
                    .setMajor(0)
                    .setMinor(0)
                    .setPatch(1)
                    .setBuild(1)
                    .setBranchName(branch);
        }

        IncreasePriority priority = IncreasePriority.NO_PRIORITY;

        for (RevCommit commit : commits) {
            String fullMessage = commit.getFullMessage();
            String[] splittedForPrefix = fullMessage.split(HEADER_DELIMETER);
            if (prefixInvalid(splittedForPrefix))
                continue;
            String prefix = splittedForPrefix[0].trim();
            IncreasePriority extractedPriority = extractPriority(prefix, RELEASE_BRANCH.equals(branch));
            if (extractedPriority.getValue() >= priority.getValue())
                priority = extractedPriority;
        }

        IncreasePriority finalPriority = priority;
        List<VersionIncreaseHandler> handlers;
        VersionIncreaseHandler defaultHandler = VersionIncreaseHandler.defaultReleaseHandler();
        if (RELEASE_BRANCH.equals(branch)) {
            handlers = VersionIncreaseHandler.releaseHandlers();
        } else {
            handlers = VersionIncreaseHandler.snapshotHandlers();
            defaultHandler = VersionIncreaseHandler.defaultSnapshotHandler();
        }

        VersionIncreaseHandler increaseHandler = handlers.stream()
                .filter(h -> h.getSupportablePriority().equals(finalPriority))
                .findFirst()
                .orElse(defaultHandler);

        return increaseHandler.handle(previousVersionInfo, branch);
    }

    private IncreasePriority extractPriority(String prefix, boolean release) {
        List<VersionIncreaseHandler> handlers = release
                ? VersionIncreaseHandler.releaseHandlers()
                : VersionIncreaseHandler.snapshotHandlers();
        return handlers.stream().filter(h -> h.getSupportablePatterns().contains(prefix))
                .findFirst()
                .map(VersionIncreaseHandler::getSupportablePriority)
                .orElse(IncreasePriority.NO_PRIORITY);
    }

    private boolean prefixInvalid(String[] prefix) {
        return prefix.length < 2;
    }
}
