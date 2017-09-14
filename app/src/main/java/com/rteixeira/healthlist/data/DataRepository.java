package com.rteixeira.healthlist.data;

import android.content.Context;
import android.net.ConnectivityManager;

import com.rteixeira.healthlist.data.source.ListDataSource;
import com.rteixeira.healthlist.data.source.impl.BasicListDataSourceImpl;
import com.rteixeira.healthlist.data.source.impl.LocalBasicListDataSourceImpl;

public class DataRepository implements ListDataSource {

    private static DataRepository INSTANCE = null;
    private final ListDataSource mRemoteDataSource;
    private final ListDataSource mLocalDataSource;

    private Context mContext;

    private DataRepository(ListDataSource mChannelsDataSource, ListDataSource mLocalDataSource) {
        this.mRemoteDataSource = mChannelsDataSource;
        this.mLocalDataSource = mLocalDataSource;
    }

    public static DataRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(BasicListDataSourceImpl.getInstance(context),
                    LocalBasicListDataSourceImpl.getInstance(context));
        }

        INSTANCE.mContext = context;
        return INSTANCE;
    }

    @Override
    public void getFacilitiesList(ListDataSourceCallback callback) {
        if (isNetworkAvailable()){
            mRemoteDataSource.getFacilitiesList(callback);
        } else {
            mLocalDataSource.getFacilitiesList(callback);
        }
    }

    private boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
