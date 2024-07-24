package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    String property, type, landlord, tenant, rooms, bathrooms, laundry, parkingSpots, numOfFloors, floorNum, unit, rent;
    TextView typeView, landlordView, tenantView, roomsView, bathroomsView, laundryView, parkingSpotsView, numOfFloorsView, floorNumView, unitView, rentView;
    Boolean hydro, heating, water;
    CheckBox chkHydro, chkHeating, chkWater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_mgmt_details);
        property = getIntent().getStringExtra("property");
        type = getIntent().getStringExtra("type");
        landlord = getIntent().getStringExtra("landlord");
        tenant = getIntent().getStringExtra("tenant");
        rooms = getIntent().getStringExtra("rooms");
        bathrooms = getIntent().getStringExtra("bathrooms");
        laundry = getIntent().getStringExtra("laundry");
        parkingSpots = getIntent().getStringExtra("parkingSpots");
        numOfFloors = getIntent().getStringExtra("numOfFloors");
        floorNum = getIntent().getStringExtra("floorNum");
        unit = getIntent().getStringExtra("unit");
        rent = getIntent().getStringExtra("rent");
        hydro = getIntent().getBooleanExtra("hydro", false);
        heating = getIntent().getBooleanExtra("heating", false);
        water = getIntent().getBooleanExtra("water", false);

        // initialize UI

        chkHydro = findViewById(R.id.propertyHydro);
        chkHeating = findViewById(R.id.propertyHeating);
        chkWater = findViewById(R.id.propertyWater);
        chkHydro.setChecked(hydro); chkHeating.setChecked(heating); chkWater.setChecked(water);
        chkHydro.setEnabled(false); chkHeating.setEnabled(false); chkWater.setEnabled(false);
        landlordView = findViewById(R.id.propertyLandlord);
        typeView = findViewById(R.id.propertyType);
        tenantView = findViewById(R.id.propertyTenant);
        roomsView = findViewById(R.id.propertyRooms);
        bathroomsView = findViewById(R.id.propertyBathrooms);
        laundryView = findViewById(R.id.propertyLaundry);
        parkingSpotsView = findViewById(R.id.propertyParking);
        numOfFloorsView = findViewById(R.id.propertyNumFloors);
        floorNumView = findViewById(R.id.propertyFloor);
        unitView = findViewById(R.id.propertyUnit);
        rentView = findViewById(R.id.propertyRent);

        switch (type) {
            case "Apartment":
                unitView.setVisibility(View.VISIBLE);
                floorNumView.setVisibility(View.VISIBLE);
                break;
            case "Basement":
            case "Studio":
            case "Townhouse":
            case "House":
                unitView.setVisibility(View.GONE);
                floorNumView.setVisibility(View.GONE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        typeView.setText("Type: " + type);
        landlordView.setText("Landlord: " + landlord);
        tenantView.setText("Tenant: " + ((tenant.isEmpty()) ? "Unassigned":tenant));
        roomsView.setText("Bedrooms: " + rooms);
        bathroomsView.setText("Bathrooms: " + bathrooms);
        laundryView.setText("Laundry: " + laundry);
        parkingSpotsView.setText("Parking Spots: " + parkingSpots);
        numOfFloorsView.setText("Num. of Floors: " + numOfFloors);
        floorNumView.setText("Floor: " + floorNum);
        rentView.setText("$" + rent);
        unitView.setText("Unit: " + unit);

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
                    intent.putExtra("type", type);
                    intent.putExtra("landlord", landlord);
                    intent.putExtra("tenant", tenant);
                    intent.putExtra("rooms", rooms);
                    intent.putExtra("bathrooms", bathrooms);
                    intent.putExtra("laundry", laundry);
                    intent.putExtra("parkingSpots", parkingSpots);
                    intent.putExtra("numOfFloors", numOfFloors);
                    intent.putExtra("floorNum", floorNum);
                    intent.putExtra("unit", unit);
                    intent.putExtra("rent", rent);
                    intent.putExtra("hydro", hydro);
                    intent.putExtra("heating", heating);
                    intent.putExtra("water", water);
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