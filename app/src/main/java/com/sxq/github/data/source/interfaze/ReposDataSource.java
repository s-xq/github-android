package com.sxq.github.data.source.interfaze;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import github.repos.GetBranchesQuery;
import github.repos.GetCommitsQuery;
import github.repos.GetContributorsQuery;
import github.repos.GetCurrentLevelTreeViewQuery;
import github.repos.GetReleasesQuery;
import io.reactivex.Observable;

public interface ReposDataSource {

    Observable<List<GetBranchesQuery.Node>> getBranches(@NonNull String owner, @NonNull String reposName);

    Observable<GetCommitsQuery.Data> getCommits(@NonNull String owner, @NonNull String reposName, @NonNull String branch, @Nullable String pageCursor);

    Observable<String> getFileContent(@NonNull String owner, @NonNull String reposName, @NonNull String branch, @NonNull String path);

    Observable<GetContributorsQuery.Data> getContributors(@NonNull String owner, @NonNull String reposName, @Nullable String pageCursor);

    Observable<List<GetCurrentLevelTreeViewQuery.Entry>> getReposFiles(@NonNull String owner, @NonNull String reposName, @NonNull String branch, @NonNull String path);

    Observable<GetReleasesQuery.Data> getReleases(@NonNull String owner, @NonNull String reposName, @Nullable String pageCursor);
}
