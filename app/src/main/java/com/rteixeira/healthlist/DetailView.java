package com.rteixeira.healthlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rteixeira.healthlist.data.model.Facility;

import static com.rteixeira.healthlist.Contracts.View.FACILITY_WORKLOAD_KEY;

public class DetailView extends AppCompatActivity {

    private Facility facility;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        facility = extras.getParcelable(FACILITY_WORKLOAD_KEY);

        setContentView(R.layout.activity_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(facility.getOrganisationType());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView facility_name = (TextView) findViewById(R.id.facility_name);
        TextView facility_type = (TextView) findViewById(R.id.facility_type);
        TextView facility_location = (TextView) findViewById(R.id.facility_location);

        Button call_btn = (Button) findViewById(R.id.call_btn);
        Button mail_btn = (Button) findViewById(R.id.mail_btn);
        Button site_btn = (Button) findViewById(R.id.site_btn);
        Button nav_btn = (Button) findViewById(R.id.nav_btn);

        facility_name.setText(facility.getOrganisationName().trim());

        if (facility.getSubType().equals(facility.getOrganisationType())) {
            facility_type.setText(String.format("%s. %s",
                    facility.getOrganisationType(), facility.getSector()).trim());
        } else {
            facility_type.setText(String.format("%s, %s. %s", facility.getSubType(),
                    facility.getOrganisationType(), facility.getSector()).trim());
        }

        facility_location.setText(String.format("%s %s %s\n%s, %s, %s", facility.getAddress1(),
                facility.getAddress2(), facility.getAddress3(), facility.getPostcode(),
                facility.getCity(), facility.getCounty()).trim());


        Uri destination = Uri.parse(String.format("google.navigation:q=%s,%s",
                String.valueOf(facility.getLatitude()), String.valueOf(facility.getLongitude())));

        final Intent mapIntent = new Intent(Intent.ACTION_VIEW, destination);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) == null || Double.isNaN(facility.getLatitude()) || Double.isNaN(facility.getLongitude())) {
            nav_btn.setVisibility(View.GONE);
        } else {
            nav_btn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    startActivity(mapIntent);
                }
            });
        }

        if (facility.getPhone().isEmpty()) {
            call_btn.setVisibility(View.GONE);
        } else {
            call_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,
                            Uri.fromParts("tel", facility.getPhone().trim(), null));
                    startActivity(intent);
                }
            });
        }

        if (facility.getEmail().isEmpty()) {
            mail_btn.setVisibility(View.GONE);
        } else {
            mail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", facility.getEmail().trim(), null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });
        }

        if (facility.getWebsite().isEmpty()) {
            site_btn.setVisibility(View.GONE);
        } else {
            site_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facility.getWebsite().trim())));
                }
            });
        }
    }
}
