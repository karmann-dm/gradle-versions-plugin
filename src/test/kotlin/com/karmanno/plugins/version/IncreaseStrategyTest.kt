package com.karmanno.plugins.version

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class IncreaseStrategyTest {

    @Test
    fun `major increase when version is provided then increase major part`() {
        // given:
        val version = VersionInfo().apply { major = 1; minor = 1; patch = 1; } // 1.1.1

        // when:
        val increased = MajorIncreaseStrategy().increase(version).toString()

        // then:
        Assertions.assertEquals("2.0.0", increased)
    }

    @Test
    fun `minor increase when version is provided then increase minor part`() {
        // given:
        val version = VersionInfo().apply { minor = 1; patch = 2 } // 0.1.2

        // when:
        val increased = MinorIncreaseStrategy().increase(version).toString()

        // then:
        Assertions.assertEquals("0.2.0", increased)
    }

    @Test
    fun `patch increase when version is provided then increase patch part`() {
        // given:
        val version = VersionInfo().apply { patch = 3; build = 1; branchName = "some" } // 0.0.3.some.1

        // when:
        val increased = PatchIncreaseStrategy().increase(version).toString()

        // then:
        Assertions.assertEquals("0.0.4.some", increased)
    }

    @Test
    fun `build increase when version is provided then increase build part`() {
        // given:
        val version = VersionInfo().apply { patch = 3; build = 1; branchName = "some" } // 0.0.3.some.1

        // when:
        val increased = BuildIncreaseStrategy().increase(version).toString()

        // then:
        Assertions.assertEquals("0.0.3.some.2", increased)
    }

    @Test
    fun `no increase when version is provided then do nothing`() {
        // given:
        val version = VersionInfo().apply { minor = 1; patch = 2 } // 0.1.2

        // when:
        val increased = NoIncreaseStrategy().increase(version).toString()

        // then:
        Assertions.assertEquals("0.1.2", increased)
    }

}