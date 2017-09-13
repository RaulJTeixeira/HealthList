package com.rteixeira.healthlist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rteixeira.healthlist.data.Facility;
import com.rteixeira.healthlist.data.source.repo.DataRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListView extends AppCompatActivity implements Contracts.View {

    private FacilityAdapter mFacilitiesAdapter;
    private Contracts.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFacilitiesAdapter = new FacilityAdapter(new ArrayList<Facility>(0));
        setPresenter(new ListPresenter(this, DataRepository.getInstance()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.requestFacilities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void setPresenter(Contracts.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void clearList() {

    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showFacilitiesList(ArrayList<Facility> facilities, boolean isFromInternet) {
        mFacilitiesAdapter.setList(facilities);
        mFacilitiesAdapter.notifyDataSetChanged();
    }

    private class FacilityAdapter extends BaseAdapter {

        private List<Facility> mFacilities;

        private FacilityAdapter(List<Facility> facilities) {
            setList(facilities);
        }

        private void setList(List<Facility> facilities) {
            mFacilities = checkNotNull(facilities);
        }

        class ViewHolder {
            //CheckedTextView checkedTextView;
        }

        @Override
        public int getCount() {
            return mFacilities.size();
        }

        @Override
        public Facility getItem(int i) {
            return mFacilities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.facility_item, parent, false);
                viewHolder = new ViewHolder();

                //FindByID for viewHolder items

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

                // set values
            return convertView;
        }
    }
}
