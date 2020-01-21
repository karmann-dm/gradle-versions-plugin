package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.IncreasePriority;
import com.karmanno.plugins.domain.VersionInfo;

import java.util.List;

public interface VersionIncreaseHandler {
    List<String> getSupportablePatterns();
    IncreasePriority getSupportablePriority();
    VersionInfo handle(VersionInfo previousVersion);

    static List<VersionIncreaseHandler> handlers() {
        return List.of(
                new MajorIncreaseHandler(),
                new MinorIncreaseHandler(),
                new PatchIncreaseHandler(),
                new BuildIncreaseHandler()
        );
    }

    static VersionIncreaseHandler defaultHandler() {
        return new NoIncreaseHandler();
    }
}
