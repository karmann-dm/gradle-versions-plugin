package com.karmanno.plugins;

import lombok.Data;

@Data
public class VersionInfo {
    private String fullVersion;
    private String branchName;
    private Integer major;
    private Integer minor;
    private Integer patch;

    private Integer build;
    private boolean isSnapshot = true;

    public static VersionInfo fromTagString(String tag) {
        return new VersionInfo();
    }
}
