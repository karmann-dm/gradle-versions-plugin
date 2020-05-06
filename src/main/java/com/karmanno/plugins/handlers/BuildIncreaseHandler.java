package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;

import java.util.Arrays;
import java.util.List;

public class BuildIncreaseHandler implements VersionIncreaseHandler {
    @Override
    public List<String> getSupportablePatterns() {
        return Arrays.asList(
                "build",
                "BUILD"
        );
    }

    @Override
    public IncreasePriority getSupportablePriority() {
        return IncreasePriority.BUILD;
    }

    @Override
    public VersionInfo handle(VersionInfo previousVersion, String branchName) {
        return new VersionInfo()
                .setMajor(previousVersion.getMajor())
                .setMinor(previousVersion.getMinor())
                .setPatch(previousVersion.getPatch())
                .setBuild(previousVersion.getBuild() == null ? 1 : previousVersion.getBuild() + 1)
                .setBranchName(branchName);
    }
}
