package com.rteixeira.healthlist.data.source;

import com.rteixeira.healthlist.data.model.Facility;

import java.util.ArrayList;

public interface ListDataSource {

    interface ListDataSourceCallback {
        void getFacilities(ArrayList<Facility> facilities, boolean isFromInternet);
        void unableToGetFacilities();
    }

    void getFacilitiesList(ListDataSourceCallback callback);
}
