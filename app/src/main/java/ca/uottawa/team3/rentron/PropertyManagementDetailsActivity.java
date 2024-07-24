package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PropertyManagementDetailsActivity extends AppCompatActivity {

    String property;
    TextView tenant, rooms, bathrooms, laundry, parkingSpots, numOfFloors, floorNum, unit, rent;
    CheckBox chkHydro, chkHeating, chkWater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_mgmt_details);
        property = getIntent().getStringExtra("property");
        //... add other extras if needed

        // initialize UI

        chkHydro = findViewById(R.id.propertyHydro);
        chkHeating = findViewById(R.id.propertyHeating);
        chkWater = findViewById(R.id.propertyWater);
        chkHydro.setClickable(false); chkHeating.setClickable(false); chkWater.setClickable(false);
        tenant = findViewById(R.id.propertyTenant);
        rooms = findViewById(R.id.propertyRooms);
        bathrooms = findViewById(R.id.propertyRooms);
        laundry = findViewById(R.id.propertyRooms);
        parkingSpots = findViewById(R.id.propertyRooms);
        numOfFloors = findViewById(R.id.propertyRooms);
        floorNum = findViewById(R.id.propertyRooms);
        unit = findViewById(R.id.propertyRooms);
        rent = findViewById(R.id.propertyRooms);

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);
        getSupportActionBar().setTitle(property);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                startActivityForResult (intent,0);
                overridePendingTransition(0,0);
                finish();
            }
        });

        // Bottom nav view/ViewCompat initialization

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.details);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.details)
                {
                    return true;
                }
                else if (item.getItemId() == R.id.tickets) {
                    Intent intent = new Intent(getApplicationContext(), PropertyManagementTicketsActivity.class);
                    intent.putExtra("property", property);
                    //... add fields as needed
                    startActivityForResult(intent, 0);
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }
}