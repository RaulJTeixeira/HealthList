package com.rteixeira.healthlist.data.source.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rteixeira.healthlist.data.model.Facility;
import com.rteixeira.healthlist.data.source.ListDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BasicListDataSourceImpl implements ListDataSource {

    private static final String FILE_URL = "https://data.gov.uk/data/resource/nhschoices/Hospital.csv";
    private static BasicListDataSourceImpl INSTANCE;

    private Context mContext;

    private BasicListDataSourceImpl() {
    }

    public static BasicListDataSourceImpl getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BasicListDataSourceImpl();
        }

        INSTANCE.mContext = context;

        return INSTANCE;
    }

    @Override
    public void getFacilitiesList(final @NonNull ListDataSourceCallback callback) {
        new DownloadFilesTask().execute(callback);
    }

    private class DownloadFilesTask extends AsyncTask<ListDataSourceCallback, Void, Void> {

        @Override
        protected Void doInBackground(ListDataSourceCallback... params) {

            if (params == null && params.length != 1) {
                return null;
            }

            ListDataSourceCallback callback = params[0];
            ArrayList<Facility> result = new ArrayList<>();

            try {

                URL url = new URL(FILE_URL);
                InputStream is = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line = reader.readLine();

                if(line != null) {
                    while ((line = reader.readLine()) != null) {
                        result.add(new Facility(line));
                    }
                }
                reader.close();
                is.close();
                callback.getFacilities(result, true);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //TODO Better way to do this (aka not call the instance from here
            LocalBasicListDataSourceImpl.getInstance(mContext).getFacilitiesList(callback);
            return null;
        }
    }
}
