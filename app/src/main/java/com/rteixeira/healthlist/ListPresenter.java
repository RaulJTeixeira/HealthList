package com.rteixeira.healthlist;

import android.app.Activity;
import android.content.Context;

import com.rteixeira.healthlist.data.DataRepository;
import com.rteixeira.healthlist.data.model.Facility;
import com.rteixeira.healthlist.data.source.ListDataSource;

import java.util.ArrayList;


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
        mRequestView.setLoadingIndicator(true);

        if (saved_facilities == null || !loadedFromInternet) {

            mRepository.getFacilitiesList(new ListDataSource.ListDataSourceCallback() {
                @Override
                public void getFacilities(final ArrayList<Facility> facilities, final boolean isFromInternet) {

                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {

                            mRequestView.setLoadingIndicator(false);
                            if (facilities.isEmpty()) {
                                mRequestView.clearList();
                                mRequestView.showLoadingError();
                            } else {
                                mRequestView.showFacilitiesList(facilities);
                            }
                        }
                    });
                }

                @Override
                public void unableToGetFacilities() {
                    mRequestView.setLoadingIndicator(false);
                    mRequestView.showLoadingError();
                }
            });

        } else {
            mRequestView.setLoadingIndicator(false);
            mRequestView.showFacilitiesList(saved_facilities);
        }
    }
}
