package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;

import java.util.List;

public interface VersionIncreaseHandler {
    List<String> getSupportablePatterns();
    IncreasePriority getSupportablePriority();
    VersionInfo handle(VersionInfo previousVersion, String branchName);

    static List<VersionIncreaseHandler> releaseHandlers() {
        return List.of(
                new MajorIncreaseHandler(),
                new MinorIncreaseHandler(),
                new PatchIncreaseHandler()
        );
    }

    static List<VersionIncreaseHandler> snapshotHandlers() {
        return List.of(
                new BuildIncreaseHandler()
        );
    }

    static VersionIncreaseHandler defaultReleaseHandler() {
        return new NoIncreaseHandler();
    }

    static VersionIncreaseHandler defaultSnapshotHandler() {
        return new BuildIncreaseHandler();
    }
}
