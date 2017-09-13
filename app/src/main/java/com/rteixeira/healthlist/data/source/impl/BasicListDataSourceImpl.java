package com.rteixeira.healthlist.data.source.impl;

import android.support.annotation.NonNull;

import com.rteixeira.healthlist.data.source.ListDataSource;

public class BasicListDataSourceImpl implements ListDataSource {

    private static BasicListDataSourceImpl INSTANCE;

    private BasicListDataSourceImpl() {}

    public static BasicListDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BasicListDataSourceImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getFacilitiesList(final @NonNull ListDataSourceCallback callback) {

    }
}
