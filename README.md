![Java CI](https://github.com/karmann-dm/gradle-versions-plugin/workflows/Java%20CI/badge.svg)
# gradle-versions-plugin

Semver plugin for gradle https://plugins.gradle.org/plugin/com.karmanno.plugins.semver

## Concepts

The general idea of the plugin is about to provide an automated tool for versions management partially based on the semantic versioning specification
concept. The basic proposition is to define two types of versions: **releases** and **snapshots**. Each of this types has 
its own structure.

### Releases

Semantically the release version should mean the atomic deployable measurement, each release has the according
deployable artifact. Usually they are not reffered to special branch, basically it is a bad practice to link
release version to anything from the development context (like branches, environments, tools, etc), so in scope of
this plugin I've made a proposition to define the following release structure:

```
1.               8.               14
^ major version  ^ minor version  ^ patch version
```

Difference:
- in **major** versions usually means that some incompatibilities in protocols, structures or APIs take place. 
- in **minor** versions shows improvements, bug fixes, minor changes but mostly with a general compatibility with previous minor versions.
- in **patch** versions are applied when you have a very short changes usually related to bug fixes or compatibility issues.



### Snapshots

Unlike release, snapshot versions are usually being used as an experimental or development deployments which are
intended to test features, provide MVPs, that means that they can be quite unstable and change frequently.
Also unlike release, sometimes it is related to development context like branches, test environment, feature name, etc.

```
2.               7.               12.              my.new.feature.  6
^ major version  ^ minor version  ^ patch version  ^ context        ^ build number
```

As a **context**, here we consider the *branch name*. Difference in **build number** usually means that it is a different variation of the same
snapshot including fixes, debug settings, test features, etc.

## Simple usage

*Note: you can clone and play around with the demo repository https://github.com/karmann-dm/gradle-semver-demo*


### Commit commands

_Note: commit commands are case-insensitive_
- To increase major version use commit message prefix `global`. Example: `global: JR-112 Major feature`
- To increase minor version use commit message prefix `feat/feature`. Example: `feat: ARD-281 Minor feature`
- To increase patch version use commit message prefix `fix`. Example: `fix: BT-81 Bug fix`

To detect the version of next release plugin scans commit list between last tag (if repo has no tags - from the very beginning of the commits list) and choose
the commit message with the highest priority prefix. You can see priorities below:
1. GLOBAL
2. FEATURE
3. PATCH

That means that if you specify 10 commits with PATCH prefix and one with GLOBAL - GLOBAL will be chosen
for the target commit command and major version will be increased.

### Import plugin

Import plugin into your project using Gradle Plugin DSL
```
plugins {
  id 'java'
  id "com.karmanno.plugins.semver" version "1.15"
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
    classpath "com.karmanno.plugins:gradle-versions-plugin:1.15"
  }
}

apply plugin: "com.karmanno.plugins.semver"
```

### To calculate and print version through console
```
gradle --console=plain -q printVersion
-> 2.10.9
```
### To calculate and print artifact version ($project_name$:$version$)
```
gradle --console=plain -q printArtifact
-> gradle-semver-demo:1.2.12
```

### To assign calculated version as a tag onto current HEAD
```
gradle assignTag

> git log
commit 86b244ddfda7470e6836fa267ad774d2e386c7dd (HEAD -> master, tag: 1.1.0, origin/master)
Author: karmann-dm <karmanno@gmail.com>
Date:   Mon May 3 16:32:43 2021 +0200

    feat: Add debug line
```
