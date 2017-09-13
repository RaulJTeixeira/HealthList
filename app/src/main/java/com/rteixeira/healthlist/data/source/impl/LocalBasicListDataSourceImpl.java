package com.rteixeira.healthlist.data.source.impl;

import com.rteixeira.healthlist.data.source.ListDataSource;


public class LocalBasicListDataSourceImpl implements ListDataSource {

    private static LocalBasicListDataSourceImpl INSTANCE;

    private LocalBasicListDataSourceImpl() {}

    public static LocalBasicListDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalBasicListDataSourceImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getFacilitiesList(ListDataSourceCallback callback) {

    }
}
