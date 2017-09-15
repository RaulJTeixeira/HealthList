package com.rteixeira.healthlist;

import android.content.Context;
import android.location.Location;

import com.rteixeira.healthlist.data.model.Facility;
import java.util.ArrayList;

public interface Contracts {

    interface View {

        String FACILITY_WORKLOAD_KEY = "facility";

        //general Setup
        void setPresenter(Presenter presenter);

        //General UI behavior
        void setLoadingIndicator(boolean active);
        void controlOptionButtons(boolean retryVisible, boolean controlOptionButtons);
        void clearList();

        //Result Control
        void showLoadingError();
        void showOfflineNotification();
        void showFacilitiesList(ArrayList<Facility> facilities);
        void showFacilityDetail(Facility facility);

        Context getContext();
    }

    interface Presenter {
        void requestFacilities();
        void requestOrderAZ();
        void requestOrderZA();
        void requestClosestFacility(Location location);
    }
}
