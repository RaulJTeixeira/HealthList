package com.rteixeira.healthlist.data.source.impl;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.rteixeira.healthlist.R;
import com.rteixeira.healthlist.data.Facility;
import com.rteixeira.healthlist.data.source.ListDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


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
        new ProcessLocalFile().execute(callback);
    }

    private class ProcessLocalFile extends AsyncTask<ListDataSourceCallback, Void, Void> {

        @Override
        protected Void doInBackground(ListDataSourceCallback... params) {

            if(params != null && params.length == 1) {

                ListDataSourceCallback callback = params[0];
                ArrayList<Facility> result = new ArrayList<>();

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.
                            getSystem().openRawResource(R.raw.hospital)));

                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.add(new Facility(line));
                    }

                    reader.close();
                    callback.getFacilities(result, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.unableToGetFacilities();
                }
            }
            return null;
        }
    }
}
