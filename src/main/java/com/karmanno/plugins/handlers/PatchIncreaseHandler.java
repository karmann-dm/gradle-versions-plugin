package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;

import java.util.Arrays;
import java.util.List;

public class PatchIncreaseHandler implements VersionIncreaseHandler {
    @Override
    public List<String> getSupportablePatterns() {
        return Arrays.asList(
                "fix",
                "FIX"
        );
    }

    @Override
    public IncreasePriority getSupportablePriority() {
        return IncreasePriority.PATCH;
    }

    @Override
    public VersionInfo handle(VersionInfo previousVersion) {
        return new VersionInfo()
                .setMajor(previousVersion.getMajor())
                .setMinor(previousVersion.getMinor())
                .setPatch(previousVersion.getPatch() + 1)
                .setBuild(0)
                .setBranchName(previousVersion.getBranchName());
    }
}
