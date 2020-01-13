package com.karmanno.plugins;

import java.util.Arrays;
import java.util.List;

public class MinorIncreaseHandler implements VersionIncreaseHandler {

    @Override
    public List<String> getSupportablePatterns() {
        return Arrays.asList(
                "feat",
                "FEATURE",
                "feature"
        );
    }

    @Override
    public IncreasePriority getSupportablePriority() {
        return IncreasePriority.MINOR;
    }

    @Override
    public VersionInfo handle(VersionInfo previousVersion) {
        return new VersionInfo()
                .setMajor(previousVersion.getMajor())
                .setMinor(previousVersion.getMinor() + 1)
                .setPatch(0)
                .setBuild(0)
                .setBranchName(previousVersion.getBranchName())
                .setReleaseBranches(previousVersion.getReleaseBranches());
    }
}
