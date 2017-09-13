package com.rteixeira.healthlist;

import android.content.Context;

import com.rteixeira.healthlist.data.Facility;
import java.util.ArrayList;

public interface Contracts {

    interface View {

        //general Setup
        void setPresenter(Presenter presenter);

        //General UI behavior
        void setLoadingIndicator(boolean active);
        void clearList();

        //Result Control
        void showLoadingError();
        void showFacilitiesList(ArrayList<Facility> facilities, boolean isFromInternet);

        Context getContext();
    }

    interface Presenter {
        void requestFacilities();
    }
}
