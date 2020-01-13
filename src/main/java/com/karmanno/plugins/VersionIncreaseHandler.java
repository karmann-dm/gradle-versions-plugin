package com.karmanno.plugins;

import java.util.List;

public interface VersionIncreaseHandler {
    List<String> getSupportablePatterns();
    IncreasePriority getSupportablePriority();
    VersionInfo handle(VersionInfo previousVersion);
}
