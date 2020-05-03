# gradle-versions-plugin

Semver plugin for gradle

## Principles of work
Plugin analyzes 'changelog' between the last tag in the branch and HEAD of the branch. Commit messages should be formatted in SemVer style.

## Examples

```
(branch master)
X -> (fix: new fix 1) -> (fix: new fix 2) -> (feature: new feature _ HEAD)
```
Then, the task `printVersion` will display version `0.1.0`
```
./gradlew printVersion -> 0.1.0
```
## FAQ
