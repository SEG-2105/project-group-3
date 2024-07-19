package ca.uottawa.team3.rentron;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Users.Tickets.*;

public class SearchActivity extends AppCompatActivity {
    List<Property> properties = new ArrayList<>();
    ListView listViewProperties;
    FirebaseFirestore firestore;
    CollectionReference db;

    // Filters
    String minFloors, minRooms, minBathrooms, minArea, minParkingSpots, minRent, maxRent, email;
    boolean basement, studio, apartment, townhouse, house, hydro, heating, water;

    // search types
    static boolean allTypes, allUtilities, byMinRent, byMaxRent, byRentRange;

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
        email = getIntent().getStringExtra("email");

        // if no types were selected, searching for all types of housing
        allTypes = !(basement || studio || apartment || townhouse || house);
        allUtilities = !(hydro || heating || water); // same with utilities

        // selecting how we are searching via rent
        if (minRent.isEmpty()) {
            byMinRent = true;
            byMaxRent = false;
            byRentRange = false;
        } else if (maxRent.isEmpty()) {
            byMinRent = false;
            byMaxRent = true;
            byRentRange = false;
        } else {
            byMinRent = false;
            byMaxRent = false;
            byRentRange = true;
        }


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
        //UNFINISHED
        db = firestore.collection("properties");
        Query search;
        if (allTypes && allUtilities) {

        }
        //UNFINISHED

        // below pulled from PropertiesActivity.java
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        listViewProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Property> property = new ArrayList<>();
                property.add(properties.get(position));
                PropertyDialogListAdapter dialogView = new PropertyDialogListAdapter(SearchActivity.this, property);
                dialogBuilder.setAdapter(dialogView, null)
                        .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Property property = properties.get(position);
                                Request request = new Request(email, property.getLandlord(), property.getAddress());

                                Courier courier = new Courier(getApplicationContext(), firestore);
                                courier.sendRequest(request);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle(properties.get(position).getAddress()).create().show();
            }
        });
    }


    private void searchViaMinRent() {

    }

    private void searchViaMaxRent() {

    }

    private void searchViaRentRange() {

    }
}