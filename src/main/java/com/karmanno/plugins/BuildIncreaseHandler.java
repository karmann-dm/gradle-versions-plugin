package com.karmanno.plugins;

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
    public VersionInfo handle(VersionInfo previousVersion) {
        return new VersionInfo()
                .setMajor(previousVersion.getMajor())
                .setMinor(previousVersion.getMinor())
                .setPatch(previousVersion.getPatch())
                .setBuild(previousVersion.getBuild() + 1)
                .setBranchName(previousVersion.getBranchName())
                .setReleaseBranches(previousVersion.getReleaseBranches());
    }
}
