package com.rteixeira.healthlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rteixeira.healthlist.data.model.Facility;

import static com.rteixeira.healthlist.Contracts.View.FACILITY_WORKLOAD;

public class DetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        Facility facility = extras.getParcelable(FACILITY_WORKLOAD);

        setContentView(R.layout.activity_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(facility.getOrganisationType());
        setSupportActionBar(toolbar);



    }
}
