# gradle-versions-plugin

Semver plugin for gradle

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

### Snapshots

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
