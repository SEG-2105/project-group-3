package ca.uottawa.team3.rentron;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    boolean basement, studio, apartment, townhouse, house, hasHydro, hasHeating, hasWater;

    // search cases
    static boolean allTypes, allUtilities;

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
        hasHydro = getIntent().getBooleanExtra("hydro",false);
        hasHeating = getIntent().getBooleanExtra("heating",false);
        hasWater = getIntent().getBooleanExtra("water",false);
        email = getIntent().getStringExtra("email");

        // if no types were selected, searching for all types of housing
        allTypes = !(basement || studio || apartment || townhouse || house);
        allUtilities = !(hasHydro || hasHeating || hasWater); // same with utilities

        // selecting how we are searching via rent
        if (minRent.isEmpty()) {
            minRent = "0";
        } else if (maxRent.isEmpty()) {
            maxRent = "999999"; // default value is set impossibly high since we are always searching in ranges
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

        listViewProperties = findViewById(R.id.propertyListView);
        // Clear the previous list
        properties.clear();

        // Iterate through all the properties
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("properties").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PropertiesActivity:", document.getId() + " => " + document.get("address"));
                                Property db_property = new Property((String) document.get("address"), (String) document.get("type"), (String) document.get("unit"), (String) document.get("floor"),
                                        (String) document.get("numRoom"), (String) document.get("numBathroom"), (String) document.get("numFloor"), (String) document.get("area"),
                                        (String) document.get("laundry"), (String) document.get("numParkingSpot"), (String) document.get("rent"), (boolean) document.get("heating"), (boolean) document.get("hydro"), (boolean) document.get("water"),
                                        (String) document.get("landlord"), (String) document.get("manager"), (String) document.get("client"));
                                if (isPropertyValid(db_property) && db_property.isRentable()) // this search will NOT show already rented properties or properties without mgr
                                {
                                    properties.add(db_property);
                                }
                                // Create the client specific adapter
                                PropertyListClient propertiesAdapter = new PropertyListClient(SearchActivity.this, properties);

                                // Attach the adapter to the list view
                                listViewProperties.setAdapter(propertiesAdapter);
                            }
                        }
                    }
                });

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
                                courier.sendMessage(request);

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

    private boolean propertyTypeCheck(Property property) {
        String propertyType = property.getType();
        switch (propertyType) {
            case "Basement":
                return basement;
            case "Studio":
                return studio;
            case "Apartment":
                return apartment;
            case "Townhouse":
                return townhouse;
            case "House":
                return house;
            default:
                throw new IllegalStateException("Unexpected value: " + propertyType);
        }
    }

    private boolean isPropertyValid(Property property) {
        int rooms = Integer.parseInt(property.getNumRoom());
        int floors = Integer.parseInt(property.getNumFloor());
        int bathrooms = Integer.parseInt(property.getNumBathroom());
        int area = Integer.parseInt(property.getArea());
        int rent = Integer.parseInt(property.getRent());
        int parkingSpots = Integer.parseInt(property.getNumParkingSpot());
        String type = property.getType();
        boolean heating = property.getHeating();
        boolean water = property.getWater();
        boolean hydro = property.getHydro();

        boolean rentCheck = false;
        boolean typeCheck = true;
        boolean attributes = false;

        // TYPE CHECK CASES NOT IMPLEMENTED YET!!
        if (allTypes && allUtilities) {
            attributes = (rooms >= Integer.parseInt(minRooms)) && (floors >= Integer.parseInt(minFloors)) && (bathrooms >= Integer.parseInt(minBathrooms))
                    && (area >= Integer.parseInt(minArea)) && (rent >= Integer.parseInt(minRent)) && (parkingSpots >= Integer.parseInt(minParkingSpots));
        } else if (allTypes) {
            attributes = (rooms >= Integer.parseInt(minRooms)) && (floors >= Integer.parseInt(minFloors)) && (bathrooms >= Integer.parseInt(minBathrooms))
                    && (area >= Integer.parseInt(minArea)) && (rent >= Integer.parseInt(minRent)) && (parkingSpots >= Integer.parseInt(minParkingSpots))
                    && (heating == hasHeating) && (hydro == hasHydro) && (water == hasWater);
        } else if (allUtilities) {
            typeCheck = false;
            attributes = (rooms >= Integer.parseInt(minRooms)) && (floors >= Integer.parseInt(minFloors)) && (bathrooms >= Integer.parseInt(minBathrooms))
                    && (area >= Integer.parseInt(minArea)) && (rent >= Integer.parseInt(minRent)) && (parkingSpots >= Integer.parseInt(minParkingSpots));
            typeCheck = propertyTypeCheck(property);
        } else {
            typeCheck = false;
            attributes = (rooms >= Integer.parseInt(minRooms)) && (floors >= Integer.parseInt(minFloors)) && (bathrooms >= Integer.parseInt(minBathrooms))
                    && (area >= Integer.parseInt(minArea)) && (rent >= Integer.parseInt(minRent)) && (parkingSpots >= Integer.parseInt(minParkingSpots))
                    && (heating == hasHeating) && (hydro == hasHydro) && (water == hasWater);
            typeCheck = propertyTypeCheck(property);
        }

        rentCheck = (rent >= Integer.parseInt(minRent)) && (rent <= Integer.parseInt(maxRent));

        return (rentCheck && attributes && typeCheck);
    }

}