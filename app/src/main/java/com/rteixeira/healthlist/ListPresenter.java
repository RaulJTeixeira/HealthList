package com.rteixeira.healthlist;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.rteixeira.healthlist.data.DataRepository;
import com.rteixeira.healthlist.data.model.Facility;
import com.rteixeira.healthlist.data.source.ListDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ListPresenter implements Contracts.Presenter {

    private Contracts.View mRequestView;
    private DataRepository mRepository;
    private Context mContext;

    private ArrayList<Facility> saved_facilities;

    private boolean loadedFromInternet = false;

    public ListPresenter(Context context, Contracts.View requestView, DataRepository repository) {
        this.mContext = context;
        this.mRequestView = requestView;
        this.mRepository = repository;
    }

    @Override
    public void requestFacilities() {

        mRequestView.clearList();
        mRequestView.controlOptionButtons(false, false);
        mRequestView.setLoadingIndicator(true);

        if (saved_facilities == null || !loadedFromInternet) {

            mRepository.getFacilitiesList(new ListDataSource.ListDataSourceCallback() {
                @Override
                public void getFacilities(final ArrayList<Facility> facilities, final boolean isFromInternet) {

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {

                        mRequestView.setLoadingIndicator(false);
                        saved_facilities = facilities;
                        loadedFromInternet = isFromInternet;
                        mRequestView.controlOptionButtons(!isFromInternet, !facilities.isEmpty());

                        if(!isFromInternet) {
                            mRequestView.showOfflineNotification();
                        }
                        mRequestView.showFacilitiesList(facilities);

                        }
                    });
                }

                @Override
                public void unableToGetFacilities() {
                    mRequestView.setLoadingIndicator(false);
                    mRequestView.showLoadingError();
                    mRequestView.controlOptionButtons(true, false);
                }
            });

        } else {
            mRequestView.setLoadingIndicator(false);
            mRequestView.showFacilitiesList(saved_facilities);
        }
    }

    @Override
    public void requestOrderAZ() {
        mRequestView.setLoadingIndicator(true);
        if(saved_facilities == null || saved_facilities.isEmpty()){
            mRequestView.showLoadingError();
            mRequestView.controlOptionButtons(true, false);
        } else {
            mRequestView.clearList();

            Collections.sort(saved_facilities, new Comparator<Facility>() {
                @Override
                public int compare(Facility f1, Facility f2) {
                    return f1.getOrganisationName().compareToIgnoreCase(f2.getOrganisationName());
                }
            });

            mRequestView.setLoadingIndicator(false);
            mRequestView.showFacilitiesList(saved_facilities);

        }
    }

    @Override
    public void requestOrderZA() {

        mRequestView.setLoadingIndicator(true);
        if(saved_facilities == null || saved_facilities.isEmpty()){
            mRequestView.showLoadingError();
            mRequestView.controlOptionButtons(true, false);
        } else {
            mRequestView.clearList();

            Collections.sort(saved_facilities, new Comparator<Facility>() {
                @Override
                public int compare(Facility f1, Facility f2) {
                    return -f1.getOrganisationName().compareToIgnoreCase(f2.getOrganisationName());
                }
            });

            mRequestView.setLoadingIndicator(false);
            mRequestView.showFacilitiesList(saved_facilities);

        }
    }

    @Override
    public void requestClosestFacility(Location user) {

        mRequestView.clearList();
        mRequestView.setLoadingIndicator(true);
        mRequestView.controlOptionButtons(false, false);

        Facility result = null;
        double distance = Double.NaN;

        for (Facility f : saved_facilities) {
            Location loc = new Location(f.getOrganisationName());
            loc.setLatitude(f.getLatitude());
            loc.setLongitude(f.getLongitude());
            double dist = user.distanceTo(loc);

            if(dist < distance || Double.isNaN(distance) ){
                result = f;
                distance = dist;
            }
        }

        mRequestView.setLoadingIndicator(false);
        mRequestView.controlOptionButtons(!loadedFromInternet, loadedFromInternet);
        mRequestView.showFacilityDetail(result);
    }
}
