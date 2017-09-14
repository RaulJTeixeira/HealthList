package com.rteixeira.healthlist;

import android.content.Context;

import com.rteixeira.healthlist.data.model.Facility;
import java.util.ArrayList;

public interface Contracts {

    interface View {

        String FACILITY_WORKLOAD_KEY = "facility";

        //general Setup
        void setPresenter(Presenter presenter);

        //General UI behavior
        void setLoadingIndicator(boolean active);
        void toggleRetryOption(boolean visible);
        void clearList();

        //Result Control
        void showLoadingError();
        void showOfflineNotification();
        void showFacilitiesList(ArrayList<Facility> facilities);

        Context getContext();
    }

    interface Presenter {
        void requestFacilities();
        void requestOrderAZ();
        void requestOrderZA();
        void requestClosestFacility();
    }
}
