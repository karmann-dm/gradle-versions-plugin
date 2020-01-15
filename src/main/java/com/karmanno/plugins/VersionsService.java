package com.karmanno.plugins;

import com.karmanno.plugins.handlers.VersionIncreaseHandler;
import org.gradle.internal.impldep.org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

class VersionsService {
    private static final String HEADER_DELIMETER = ":";

    VersionInfo calculateNewVersions(VersionInfo previousVersionInfo,
                                     Iterable<RevCommit> commits) {

        IncreasePriority priority = IncreasePriority.NO_PRIORITY;

        for (RevCommit commit : commits) {
            String fullMessage = commit.getFullMessage();
            String[] splittedForPrefix = fullMessage.split(HEADER_DELIMETER);
            if (prefixInvalid(splittedForPrefix))
                continue;
            String prefix = splittedForPrefix[0].trim();
            IncreasePriority extractedPriority = extractPriority(prefix);
            if (extractedPriority.getValue() >= priority.getValue())
                priority = extractedPriority;
        }

        IncreasePriority finalPriority = priority;
        VersionIncreaseHandler increaseHandler = VersionIncreaseHandler.handlers().stream()
                .filter(h -> h.getSupportablePriority().equals(finalPriority))
                .findAny()
                .orElse(VersionIncreaseHandler.defaultHandler());

        return increaseHandler.handle(previousVersionInfo);
    }

    private IncreasePriority extractPriority(String prefix) {
        List<VersionIncreaseHandler> handlers = VersionIncreaseHandler.handlers();
        return handlers.stream().filter(h -> h.getSupportablePatterns().contains(prefix))
                .findFirst()
                .map(VersionIncreaseHandler::getSupportablePriority)
                .orElse(IncreasePriority.NO_PRIORITY);
    }

    private boolean prefixInvalid(String[] prefix) {
        return prefix.length < 2;
    }
}
