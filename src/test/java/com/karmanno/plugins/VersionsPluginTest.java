package com.karmanno.plugins;

import nebula.test.PluginProjectSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class VersionsPluginTest extends PluginProjectSpec {
    @Override
    public String getPluginName() {
        return "com.karmanno.plugins.versions";
    }

    @Test
    public void test() {

    }
}
