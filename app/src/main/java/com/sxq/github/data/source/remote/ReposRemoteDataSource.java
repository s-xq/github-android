package com.sxq.github.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.sxq.github.data.source.interfaze.ReposDataSource;
import com.sxq.github.provider.network.graphql.ApolloProvider;

import java.util.List;

import github.repos.GetBranchesQuery;
import github.repos.GetCommitsQuery;
import github.repos.GetContributorsQuery;
import github.repos.GetCurrentLevelTreeViewQuery;
import github.repos.GetFileContentQuery;
import github.repos.GetReleasesQuery;
import io.reactivex.Observable;

public class ReposRemoteDataSource implements ReposDataSource {

    @Nullable
    private static ReposRemoteDataSource INSTANCE;

    public static ReposRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (ReposRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReposRemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private ReposRemoteDataSource() {
    }

    @Override
    public Observable<List<GetBranchesQuery.Node>> getBranches(@NonNull String owner, @NonNull String reposName) {
        ApolloCall<GetBranchesQuery.Data> apolloCall = ApolloProvider.getApolloInstance()
                .query(GetBranchesQuery.builder()
                        .owner(owner)
                        .reposName(reposName)
                        .build());
        return Rx2Apollo.from(apolloCall)
                .filter(dataResponse -> !dataResponse.hasErrors())
                .flatMap(dataResponse -> {
                    if (dataResponse.data() != null && dataResponse.data().repository() != null && dataResponse.data().repository().refs() != null) {
                        return Observable.fromIterable(dataResponse.data().repository().refs().nodes());
                    }
                    return Observable.empty();
                })
                .toList()
                .toObservable();
    }

    @Override
    public Observable<GetCommitsQuery.Data> getCommits(@NonNull String owner, @NonNull String reposName, @NonNull String branch, @Nullable String pageCursor) {
        ApolloCall<GetCommitsQuery.Data> apolloCall = ApolloProvider.getApolloInstance()
                .query(GetCommitsQuery.builder()
                        .owner(owner)
                        .reposName(reposName)
                        .branch(branch)
                        .pageCursor(pageCursor)
                        .build());
        return Rx2Apollo.from(apolloCall)
                .filter(dataResponse -> !dataResponse.hasErrors())
                .map(dataResponse -> dataResponse.data());
    }

    @Override
    public Observable<String> getFileContent(@NonNull String owner, @NonNull String reposName, @NonNull String branch, @NonNull String path) {
        ApolloCall<GetFileContentQuery.Data> apolloCall = ApolloProvider.getApolloInstance()
                .query(GetFileContentQuery.builder()
                        .owner(owner)
                        .reposName(reposName)
                        .expression(String.format("%s:%s", branch, path))
                        .build());
        return Rx2Apollo.from(apolloCall)
                .filter(dataResponse -> !dataResponse.hasErrors())
                .map(dataResponse -> dataResponse.data().repository().object().asBlob().text());
    }

    @Override
    public Observable<GetContributorsQuery.Data> getContributors(@NonNull String owner, @NonNull String reposName, @Nullable String pageCursor) {
        ApolloCall<GetContributorsQuery.Data> apolloCall = ApolloProvider.getApolloInstance()
                .query(GetContributorsQuery.builder()
                        .owner(owner)
                        .reposName(reposName)
                        .pageCursor(pageCursor)
                        .build());
        return Rx2Apollo.from(apolloCall)
                .filter(dataResponse -> !dataResponse.hasErrors())
                .map(dataResponse -> dataResponse.data());
    }

    @Override
    public Observable<List<GetCurrentLevelTreeViewQuery.Entry>> getReposFiles(@NonNull String owner, @NonNull String reposName, @NonNull String branch, @NonNull String path) {
        ApolloCall<GetCurrentLevelTreeViewQuery.Data> apolloCall = ApolloProvider.getApolloInstance()
                .query(GetCurrentLevelTreeViewQuery.builder()
                        .owner(owner)
                        .reposName(reposName)
                        .expression(String.format("%s:%s", branch, path == null ? "" : path))
                        .build());
        return Rx2Apollo.from(apolloCall)
                .filter(dataResponse -> !dataResponse.hasErrors())
                .map(dataResponse -> dataResponse.data().repository().object().asTree().entries());

    }

    @Override
    public Observable<GetReleasesQuery.Data> getReleases(@NonNull String owner, @NonNull String reposName, @Nullable String pageCursor) {
        ApolloCall<GetReleasesQuery.Data> apolloCall = ApolloProvider.getApolloInstance()
                .query(GetReleasesQuery.builder()
                        .owner(owner)
                        .reposName(reposName)
                        .pageCursor(pageCursor)
                        .build());
        return Rx2Apollo.from(apolloCall)
                .filter(dataResponse -> !dataResponse.hasErrors())
                .map(dataResponse -> dataResponse.data());

    }
}
