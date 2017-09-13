package com.rteixeira.healthlist.data.source.repo;

import com.rteixeira.healthlist.data.source.ListDataSource;
import com.rteixeira.healthlist.data.source.impl.BasicListDataSourceImpl;

public class DataRepository implements ListDataSource {

    private static DataRepository INSTANCE = null;
    private final ListDataSource mListDataSource;

    private DataRepository(ListDataSource mChannelsDataSource) {
        this.mListDataSource = mChannelsDataSource;
    }

    public static DataRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(BasicListDataSourceImpl.getInstance());
        }
        return INSTANCE;
    }

    @Override
    public void getFacilitiesList(ListDataSourceCallback callback) {
        mListDataSource.getFacilitiesList(callback);
    }
}
