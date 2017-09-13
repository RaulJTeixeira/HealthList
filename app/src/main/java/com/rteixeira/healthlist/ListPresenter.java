package com.rteixeira.healthlist;

import com.rteixeira.healthlist.data.Facility;
import com.rteixeira.healthlist.data.source.ListDataSource;
import com.rteixeira.healthlist.data.source.repo.DataRepository;

import java.util.ArrayList;


public class ListPresenter implements Contracts.Presenter {

    private Contracts.View mRequestView;
    private DataRepository mRepository;

    public ListPresenter(Contracts.View requestView, DataRepository repository) {
        this.mRequestView = requestView;
        this.mRepository = repository;
    }

    @Override
    public void requestFacilities() {

        mRequestView.clearList();
        mRequestView.setLoadingIndicator(true);

        mRepository.getFacilitiesList(new ListDataSource.ListDataSourceCallback() {
            @Override
            public void getFacilities(ArrayList<Facility> facilities, boolean fromInternet) {
                mRequestView.showFacilitiesList(facilities, fromInternet);
                mRequestView.setLoadingIndicator(false);
            }

            @Override
            public void unableToGetFacilities() {
                mRequestView.setLoadingIndicator(false);
                mRequestView.showLoadingError();
            }
        });

    }
}
