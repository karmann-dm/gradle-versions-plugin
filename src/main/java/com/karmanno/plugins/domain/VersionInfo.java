package com.karmanno.plugins.domain;

import lombok.Data;

@Data
public class VersionInfo {
    private static final String DOT = ".";
    private String branchName;
    private Integer major;
    private Integer minor;
    private Integer patch;
    private Integer build;

    public VersionInfo() {
        init();
    }

    private void init() {
        branchName = "master";
        major = 0;
        minor = 0;
        patch = 1;
        build = 1;
    }

    public String printVersion() {
        StringBuilder version = new StringBuilder()
                .append(major).append(DOT).append(minor).append(DOT).append(patch);
        if (!"master".equals(branchName)) {
            version.append(DOT).append(branchName
                    .replace(" ", DOT)
                    .replace("-", DOT)
            );
            version.append(DOT).append(build);
        }
        return version.toString();
    }

    /**
     * Release: 1.2.3
     * Snapshot 1.2.3.branch.123
     */
    public static VersionInfo fromTagString(String tag) {
        String[] tokens = tag.split("\\.");
        Integer major = Integer.parseInt(tokens[0]);
        Integer minor = Integer.parseInt(tokens[1]);
        Integer patch = Integer.parseInt(tokens[2]);
        Integer build = null;
        String branch = "master";

        if (tokens.length == 5) { // snapshot
            branch = tokens[3];
            build = Integer.parseInt(tokens[4]);
        }

        return new VersionInfo()
                .setBranchName(branch)
                .setMajor(major)
                .setMinor(minor)
                .setPatch(patch)
                .setBuild(build);
    }

    public boolean isSnapshot() {
        return "master".equals(branchName);
    }
}
