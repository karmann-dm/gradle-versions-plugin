package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;

import java.util.Collections;
import java.util.List;

public class NoIncreaseHandler implements VersionIncreaseHandler {
    @Override
    public List<String> getSupportablePatterns() {
        return Collections.emptyList();
    }

    @Override
    public IncreasePriority getSupportablePriority() {
        return null;
    }

    @Override
    public VersionInfo handle(VersionInfo previousVersion, String branchName) {
        return previousVersion;
    }
}
