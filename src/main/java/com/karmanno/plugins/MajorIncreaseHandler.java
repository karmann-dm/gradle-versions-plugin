package com.karmanno.plugins;

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
    }
}
