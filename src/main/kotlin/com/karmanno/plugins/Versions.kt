package com.karmanno.plugins

class VersionInfo(
    val branchName: String = DEFAULT_BRANCH,
    var major: Int = 0,
    var minor: Int = 0,
    var patch: Int = 0,
    var build: Int? = null
) {
    companion object {
        const val DOT = "."
        const val DEFAULT_BRANCH = "master"

        fun fromString(tag: String): VersionInfo {
            val tagString = if(isGlobal(tag)) { tag.split("/").last() } else tag

            return try {
                val tokens = tagString.split(".")
                val (major, minor, patch) = Triple(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
                val (branch, build) = if (tokens.size == 5) Pair(tokens[3], tokens[4].toInt()) else Pair(null, null)

                VersionInfo(
                    branchName = branch ?: DEFAULT_BRANCH,
                    major = major, minor = minor, patch = patch, build = build
                )
            } catch (e: Exception) {
                VersionInfo()
            }
        }

        private fun isGlobal(tag: String): Boolean = tag.split("/").size == 3
    }

    override fun toString(): String = StringBuilder().apply {
        append(major).append(DOT).append(minor).append(DOT).append(patch)
        if (isSnapshot()) {
            append(DOT).append(
                branchName.replace(" ", DOT).replace("-", DOT)
            )
        }
    }.toString()

    private fun isSnapshot(): Boolean = branchName != DEFAULT_BRANCH

}

enum class IncreasePriority(val value: Int) {
    MAJOR(4), MINOR(3), PATCH(2), BUILD(1), NO_PRIORITY(0);
}