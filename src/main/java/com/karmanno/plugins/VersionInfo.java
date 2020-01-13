package com.karmanno.plugins;

import lombok.Data;

import java.util.Arrays;
import java.util.Collections;

/**
 * Full version snapshot with some branch: 1.2.3.feature-account.153
 * Full version snapshot with master: 1.2.3.153
 * Full version release with some branch: 1.2.3.feature-account
 * Full version release with master: 1.2.3
 */
@Data
public class VersionInfo {
    private static final String DOT = ".";

    private String[] releaseBranches;
    private String branchName;
    private Integer major;
    private Integer minor;
    private Integer patch;
    private Integer build;
    private boolean isSnapshot;

    public VersionInfo() {
        init();
    }

    public VersionInfo(Integer major, Integer minor, Integer patch, Integer build, String branchName) {
    }

    public VersionInfo(String... releaseBranches) {
        init();
        this.releaseBranches = releaseBranches;
    }

    private void init() {
        branchName = "master";
        releaseBranches = (String[]) Collections.singletonList(branchName).toArray();
        major = 0;
        minor = 0;
        patch = 1;
        build = 1;
        isSnapshot = false;
    }

    public String printVersion() {
        StringBuilder version = new StringBuilder(major)
                .append(DOT).append(minor).append(DOT).append(patch);
        if (!Arrays.asList(releaseBranches).contains(branchName)) {
            version.append(DOT).append(branchName);
        }
        if (isSnapshot) {
            version.append(DOT).append(build);
        }
        return version.toString();
    }

    public static VersionInfo fromTagString(String tag) {
        String[] tokens = tag.split("\\.");
        Integer major = Integer.parseInt(tokens[0]);
        return new VersionInfo();
    }
}
