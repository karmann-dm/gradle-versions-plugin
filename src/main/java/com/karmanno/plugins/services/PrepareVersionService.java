package com.karmanno.plugins.services;

import com.karmanno.plugins.domain.VersionInfo;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public class PrepareVersionService {
    @SneakyThrows
    public VersionInfo getVersion(Git git) {
        String branchName = git.getRepository().getBranch();

        List<Ref> tagList = git.tagList().call();

        if(tagList.size() == 0)
            return new VersionInfo().setBranchName(branchName);

        Ref latestTag = tagList.get(tagList.size() - 1);
        VersionInfo latestVersionInfo = VersionInfo.fromTagString(latestTag.getName());

        ObjectId headId = git.getRepository().resolve(Constants.HEAD);
        Iterable<RevCommit> commitsBetweenHeadAndLatestTag = git.log()
                .addRange(git.getRepository().peel(latestTag).getPeeledObjectId(), headId)
                .call();

        return new VersionsService()
                .calculateNewVersions(latestVersionInfo, branchName, commitsBetweenHeadAndLatestTag);
    }
}
