package com.karmanno.plugins.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class VersionInfoTest {
    @Test
    public void testInit() {
        VersionInfo versionInfo = new VersionInfo();

        assertEquals(0, versionInfo.getMajor().intValue());
        assertEquals(0, versionInfo.getMinor().intValue());
        assertEquals(1, versionInfo.getPatch().intValue());
        assertEquals(1, versionInfo.getBuild().intValue());
        assertEquals("master", versionInfo.getBranchName());
    }

    @Test
    public void testPrintVersion_release() {
        VersionInfo info = new VersionInfo()
                .setMajor(1)
                .setMinor(2)
                .setPatch(3)
                .setBranchName("master");

        String versionStr = info.printVersion();

        assertEquals("1.2.3", versionStr);
    }

    @Test
    public void testPrintVersion_snapshot() {
        VersionInfo info = new VersionInfo()
                .setMajor(1)
                .setMinor(2)
                .setPatch(3)
                .setBranchName("dev")
                .setBuild(12);

        String versionStr = info.printVersion();

        assertEquals("1.2.3.dev.12", versionStr);
    }

    @Test
    public void testPrintVersion_snapshot_withSpaces() {
        VersionInfo info = new VersionInfo()
                .setMajor(1)
                .setMinor(2)
                .setPatch(3)
                .setBranchName("dev new version")
                .setBuild(12);

        String versionStr = info.printVersion();

        assertEquals("1.2.3.dev.new.version.12", versionStr);
    }

    @Test
    public void testPrintVersion_snapshot_withDashes() {
        VersionInfo info = new VersionInfo()
                .setMajor(1)
                .setMinor(2)
                .setPatch(3)
                .setBranchName("dev-new-version")
                .setBuild(12);

        String versionStr = info.printVersion();

        assertEquals("1.2.3.dev.new.version.12", versionStr);
    }

    @Test
    public void testFromTagString_release() {
        String versionString = "1.2.3";

        VersionInfo info = VersionInfo.fromTagString(versionString);

        assertEquals(1, info.getMajor().intValue());
        assertEquals(2, info.getMinor().intValue());
        assertEquals(3, info.getPatch().intValue());
        assertNull(info.getBuild());
        assertEquals("master", info.getBranchName());
    }

    @Test
    public void testFromTagString_snapshot() {
        String versionString = "1.2.3.dev.4";

        VersionInfo info = VersionInfo.fromTagString(versionString);

        assertEquals(1, info.getMajor().intValue());
        assertEquals(2, info.getMinor().intValue());
        assertEquals(3, info.getPatch().intValue());
        assertEquals(4, info.getBuild().intValue());
        assertEquals("dev", info.getBranchName());
    }
}
