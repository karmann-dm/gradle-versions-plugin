package com.karmanno.plugins.version

interface IncreaseStrategy {
    fun supportablePatterns(): Set<String>
    fun supportablePriority(): IncreasePriority
    fun increase(previousVersion: VersionInfo): VersionInfo

    companion object {
        fun release() = listOf(
            MajorIncreaseStrategy(), MinorIncreaseStrategy(), PatchIncreaseStrategy(), BuildIncreaseStrategy()
        )

        fun snapshot() = listOf(BuildIncreaseStrategy())
    }
}

class MajorIncreaseStrategy: IncreaseStrategy {
    override fun supportablePatterns(): Set<String> = setOf("global")
    override fun supportablePriority(): IncreasePriority = IncreasePriority.MAJOR
    override fun increase(previousVersion: VersionInfo): VersionInfo = previousVersion.apply {
        major += 1
        minor = 0
        patch = 0
        build = 0
    }
}

class MinorIncreaseStrategy: IncreaseStrategy {
    override fun supportablePatterns(): Set<String> = setOf("feat", "feature")
    override fun supportablePriority(): IncreasePriority = IncreasePriority.MINOR
    override fun increase(previousVersion: VersionInfo): VersionInfo = previousVersion.apply {
        minor += 1
        patch = 0
        build = 0
    }
}

class PatchIncreaseStrategy: IncreaseStrategy {
    override fun supportablePatterns(): Set<String> = setOf("patch", "fix", "chore")
    override fun supportablePriority(): IncreasePriority = IncreasePriority.PATCH
    override fun increase(previousVersion: VersionInfo): VersionInfo = previousVersion.apply {
        patch += 1
        build = 0
    }
}

class BuildIncreaseStrategy: IncreaseStrategy {
    override fun supportablePatterns(): Set<String> = setOf("build")
    override fun supportablePriority(): IncreasePriority = IncreasePriority.BUILD
    override fun increase(previousVersion: VersionInfo): VersionInfo = previousVersion.apply { build = build?.plus(1) }
}

class NoIncreaseStrategy: IncreaseStrategy {
    override fun supportablePatterns(): Set<String> = setOf("no", "nothing")
    override fun supportablePriority(): IncreasePriority = IncreasePriority.NO_PRIORITY
    override fun increase(previousVersion: VersionInfo): VersionInfo = previousVersion
}