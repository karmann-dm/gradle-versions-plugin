# gradle-versions-plugin

Semver plugin for gradle

## Simple usage

Import plugin into your project using Gradle Plugin DSL
```
plugins {
  id "com.karmanno.plugins.semver" version "1.2"
}
```
Or using legacy Gradle plugins API
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.karmanno.plugins:gradle-versions-plugin:1.2"
  }
}

apply plugin: "com.karmanno.plugins.semver"
```
Then, you will have access to the task `printVersion`, which is designed for the printing the next version
of your project. This version can be recorded in any CI/CD pipeline and it can be used for tagging any artifacts or git commits.

## Principles of work

### Version structure
```
1        .2         .3       .dev      .321
major     minor      patch    branch    build
```

### Versioning

Plugin analyzes 'changelog' between the last tag in the branch and HEAD of the branch. Commit messages should be formatted in SemVer style.
The decision of which commit should be chosen as prior is making based on the priority principle. There is a list of priorities of commit types sorted in descending order.

1. `global/GLOBAL` - increase of the major version
2. `feature/feat/FEATURE` - increase of the minor version
3. `fix/FIX` - increase of the patch version
4. `build/BUILD` - increase build subversion

Version numbers start from the basic start - version `0.0.1`. After this version, plugin will generate new version according to the commit list. This version can be printed by calling `printVersion` task and it can be used in further CI/CD pipelines for automatic versioning of your project.

### Releases

By default commits, listed in `master` branch are counted as release commits, otherwise - as snapshot commits. Release version consists of only `major`, `minor` and `patch` version components. For example version `2.4.12` can be considered as release version.
In the release versioning strategy there is several ways to increase version elements. 

### Snapshots

Snapshot version consists of `major`, `minor`, `patch`, `branch` and `build` version components. For example version `2.10.43.dev.32` can be considered as snapshot version.

## Examples

## FAQ
