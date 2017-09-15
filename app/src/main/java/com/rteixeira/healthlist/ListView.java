package com.rteixeira.healthlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

    private static final int REQUEST_NETWORK_LOCATION = 0x01;
    private FacilityAdapter mFacilitiesAdapter;
    private Contracts.Presenter mPresenter;

    private ProgressBar mLoader;
    private android.widget.ListView mListView;

    private boolean showRetryButton = false;
    private boolean showFilterButtons = false;

    private LocationManager mLocationManager;

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
                bundle.putParcelable(FACILITY_WORKLOAD_KEY, mFacilitiesAdapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.requestFacilities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        menu.findItem(R.id.action_retry).setVisible(showRetryButton);
        menu.findItem(R.id.action_orderAZ).setVisible(showFilterButtons);
        menu.findItem(R.id.action_orderZA).setVisible(showFilterButtons);
        menu.findItem(R.id.action_getClosest).setVisible(showFilterButtons);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_retry:
                mPresenter.requestFacilities();
                break;
            case R.id.action_orderAZ:
                mPresenter.requestOrderAZ();
                break;
            case R.id.action_orderZA:
                mPresenter.requestOrderZA();
                break;
            case R.id.action_getClosest:
                handleLocationRequest();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleLocationRequest() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_NETWORK_LOCATION);
        } else {
            clearList();
            setLoadingIndicator(true);
            controlOptionButtons(false, false);
            mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener, null);
        }
    }


    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mPresenter.requestClosestFacility(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

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
    public void controlOptionButtons(boolean retyrVisible, boolean filterVisible) {
        showRetryButton = retyrVisible;
        showFilterButtons = filterVisible;
        invalidateOptionsMenu();
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
        Snackbar.make(mListView, R.string.network_error,
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

    @Override
    public void showFacilityDetail(Facility facility) {
        Intent intent = new Intent(ListView.this, DetailView.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(FACILITY_WORKLOAD_KEY, facility);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_NETWORK_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleLocationRequest();
                } else {
                    Snackbar.make(mListView, R.string.location_request_error,
                            Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handleLocationRequest();
                        }
                    }).show();
                }
            }
        }
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

            if (mFacility.getCounty().isEmpty()) {
                if (!mFacility.getCity().isEmpty()) {
                    viewHolder.facility_city.setText(mFacility.getCity());
                }
            } else {

                if (mFacility.getCity().isEmpty()) {
                    viewHolder.facility_city.setText(mFacility.getCounty());
                } else {
                    viewHolder.facility_city.setText(String.format(Locale.getDefault(), "%s, %s",
                            mFacility.getCounty(), mFacility.getCity()));
                }
            }
            return convertView;
        }
    }
}
