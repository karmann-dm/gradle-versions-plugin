package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;

import java.util.Arrays;
import java.util.List;

public class MajorIncreaseHandler implements VersionIncreaseHandler {

    @Override
    public List<String> getSupportablePatterns() {
        return Arrays.asList(
                "global",
                "GLOBAL"
        );
    }

    @Override
    public IncreasePriority getSupportablePriority() {
        return IncreasePriority.MAJOR;
    }

    @Override
    public VersionInfo handle(VersionInfo previousVersion) {
        return new VersionInfo()
                .setMajor(previousVersion.getMajor() + 1)
                .setMinor(0)
                .setPatch(0)
                .setBuild(0)
                .setBranchName(previousVersion.getBranchName());
    }
}
