package com.rteixeira.healthlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rteixeira.healthlist.data.DataRepository;
import com.rteixeira.healthlist.data.model.Facility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListView extends AppCompatActivity implements Contracts.View {

    private FacilityAdapter mFacilitiesAdapter;
    private Contracts.Presenter mPresenter;

    private ProgressBar mLoader;
    private android.widget.ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.list_title);
        setSupportActionBar(toolbar);

        mFacilitiesAdapter = new FacilityAdapter(new ArrayList<Facility>(0));
        setPresenter(new ListPresenter(this, this, DataRepository.getInstance(getContext())));

        mLoader = (ProgressBar) findViewById(R.id.loader);
        mListView = (android.widget.ListView) findViewById(R.id.facilities_list);
        mListView.setAdapter(mFacilitiesAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListView.this, DetailView.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(FACILITY_WORKLOAD, mFacilitiesAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
        mLoader.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void clearList() {
        mFacilitiesAdapter.setList(new ArrayList<Facility>());
        mFacilitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingError() {
        Snackbar.make(mListView, R.string.failed_to_load,
                Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.requestFacilities();
                    }
                }).show();
    }

    @Override
    public void showOfflineNotification() {
        Snackbar.make(mListView, "Failed to load content Online, used offline source.",
                Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.requestFacilities();
                    }
                }).show();
    }

    @Override
    public void showFacilitiesList(ArrayList<Facility> facilities) {
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
            TextView facility_name;
            TextView facility_type;
            TextView facility_city;
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
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.facility_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.facility_name = (TextView) convertView.findViewById(R.id.item_name);
                viewHolder.facility_type = (TextView) convertView.findViewById(R.id.item_type);
                viewHolder.facility_city = (TextView) convertView.findViewById(R.id.item_city);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Facility mFacility = getItem(position);
            viewHolder.facility_name.setText(mFacility.getOrganisationName());
            viewHolder.facility_type.setText(mFacility.getOrganisationType());
            viewHolder.facility_city.setText(String.format(Locale.getDefault(),"%s, %s",
                    mFacility.getCounty(), mFacility.getCity()));

            return convertView;
        }
    }
}
