package com.karmanno.plugins.handlers;

import com.karmanno.plugins.domain.VersionInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class PatchIncreaseHandlerTest {
    @Test
    public void increaseTest() {
        PatchIncreaseHandler increaseHandler = new PatchIncreaseHandler();
        VersionInfo info = VersionInfo.fromTagString("1.2.3.dev.4");

        VersionInfo newInfo = increaseHandler.handle(info);

        assertEquals(1, newInfo.getMajor().intValue());
        assertEquals(2, newInfo.getMinor().intValue());
        assertEquals(4, newInfo.getPatch().intValue());
        assertEquals("dev", newInfo.getBranchName());
        assertEquals(0, newInfo.getBuild().intValue());
    }
}
