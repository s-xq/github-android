package com.sxq.github.ui.modules.profile.overview;

import com.sxq.github.data.source.interfaze.UserSourceRepository;
import com.sxq.github.data.source.local.UserLocalDataSource;
import com.sxq.github.data.source.remote.UserRemoteDataSource;

/**
 * Created by shixiaoqiang01 on 2018/5/18.
 */

public class ProfileOverViewModule {

    public static ProfileOverViewViewModel createViewModel() {
        return new ProfileOverViewViewModel(
                UserSourceRepository.getInstance(
                        UserLocalDataSource.getInstance(), UserRemoteDataSource.getInstance()));
    }

}
