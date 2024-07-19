package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Users.Tickets.*;

public class SearchActivity extends AppCompatActivity {
    List<Property> properties = new ArrayList<>();
    ListView listViewProperties;
    FirebaseFirestore firestore;

    // Filters
    String minFloors, minRooms, minBathrooms, minArea, minParkingSpots, minRent, maxRent;
    boolean basement, studio, apartment, townhouse, house, hydro, heating, water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);
        getSupportActionBar().setTitle("Advanced Search Results");

        // unpack bundled extras from intent
        minFloors = getIntent().getStringExtra("minFloors");
        minRooms = getIntent().getStringExtra("minRooms");
        minBathrooms = getIntent().getStringExtra("minBathrooms");
        minArea = getIntent().getStringExtra("minArea");
        minParkingSpots = getIntent().getStringExtra("minParkingSpots");
        minRent = getIntent().getStringExtra("minRent");
        maxRent = getIntent().getStringExtra("maxRent");
        basement = getIntent().getBooleanExtra("typeBasement",false);
        studio = getIntent().getBooleanExtra("typeStudio",false);
        apartment = getIntent().getBooleanExtra("typeApartment",false);
        townhouse = getIntent().getBooleanExtra("typeTownhouse",false);
        house = getIntent().getBooleanExtra("typeHouse",false);
        hydro = getIntent().getBooleanExtra("hydro",false);
        heating = getIntent().getBooleanExtra("heating",false);
        water = getIntent().getBooleanExtra("water",false);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                startActivityForResult (intent,0);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        listViewProperties = findViewById(R.id.listViewProperties);
        // Clear the previous list
        properties.clear();

        // Iterate through all the properties
        firestore = FirebaseFirestore.getInstance();
        // ...
    }
}