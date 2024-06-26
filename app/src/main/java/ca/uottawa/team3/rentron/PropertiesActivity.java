package ca.uottawa.team3.rentron;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.uottawa.team3.rentron.Properties.Property;

public class PropertiesActivity extends AppCompatActivity {
    private SharedPreferences pref;
    Button btnProperty;
    List<Property> properties = new ArrayList<>();
    ListView listViewProperties;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_properties);
        properties = new ArrayList<Property>();
        Toast.makeText(getApplicationContext(), "Properties list created", Toast.LENGTH_SHORT).show();

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.properties);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.properties)
                {
                    return true;
                }
                else if (item.getItemId() == R.id.welcome) {
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
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

    // ensure that navigation view is in correct position when returning to activity (via Back button)
    @Override
    public void onStart() {
        super.onStart();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.properties);

        listViewProperties = findViewById(R.id.listViewProperties);
        // Clear the previous list
        properties.clear();

        // Iterate through all the properties
        firestore = FirebaseFirestore.getInstance();

        btnProperty = findViewById(R.id.btnViewProperty);

        byte[] active = Base64.decode(pref.getString("active", ""), Base64.DEFAULT);
        byte[] active1 = Base64.decode(pref.getString("activeRole", ""), Base64.DEFAULT);

        String activeEmail = "";
        String activeRole = "";
        try {
            activeEmail = new String(active, "UTF-8");
            activeRole = new String(active1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String role = activeRole;
        String email = activeEmail;

        if (role.equals("landlord")) {
            btnProperty.setVisibility(View.VISIBLE);
        }

        firestore.collection("properties").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PropertiesActivity:", document.getId() + " => " + document.get("address"));
                                Property db_property = new Property((String)document.get("address"), (String)document.get("type"), (String)document.get("unit"), (String)document.get("floor"),
                                        (String)document.get("numRoom"), (String)document.get("numBathroom"), (String)document.get("numFloor"), (String)document.get("area"),
                                        (String)document.get("laundry"), (String)document.get("numParkingSpot"), (String)document.get("rent"), (boolean)document.get("heating"), (boolean)document.get("hydro"), (boolean)document.get("water"),
                                        (String)document.get("landlord"),(String)document.get("manager"),(String)document.get("client"));
                                if (role.equals("landlord")) {
                                    if (db_property.getLandlord().equals(email)){
                                        properties.add(db_property);
                                    }
                                } else if (role.equals("client")) {
                                    if (db_property.getClient().isEmpty() && !db_property.getManager().isEmpty()){
                                        properties.add(db_property);
                                    }
                                } else {
                                    properties.add(db_property);
                                }

                                //Toast.makeText(getApplicationContext(), "Property added.", Toast.LENGTH_SHORT).show();
                            }

                            if (role.equals("landlord")) {
                                // Create the adapter
                                PropertyListLandlord propertiesAdapter = new PropertyListLandlord(PropertiesActivity.this, properties);

                                // Attach the adapter to the list view
                                listViewProperties.setAdapter(propertiesAdapter);
                            } else {
                                // Create the adapter
                                PropertyList propertiesAdapter = new PropertyList(PropertiesActivity.this, properties);

                                // Attach the adapter to the list view
                                listViewProperties.setAdapter(propertiesAdapter);
                            }

                        } else {
                            Log.d("PropertiesActivity:", "Error getting properties: ", task.getException());
                        }
                    }
                });

        // ONLY LANDLORDS SHOULD HAVE ACCESS TO THIS BUTTON
        btnProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterPropertyActivity.class);
                startActivityForResult (intent,0);
            }
        });

        listViewProperties.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Property property = properties.get(i);
                if (email.equals(property.getLandlord()) || email.equals(property.getManager())) {
                    Intent intent = new Intent(getApplicationContext(), EditPropertyActivity.class);
                    intent.putExtra("property", property.getAddress());
                    startActivityForResult(intent, 0);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Can't edit this property, you do not own it.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //LayoutInflater inflater = null;
        //View dialogView = inflater.inflate(R.layout.layout_property_item, null);
        //dialogBuilder.setView(dialogView);
        if (role.equals("client")) {
            listViewProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<Property> property = new ArrayList<>();
                    property.add(properties.get(position));
                    PropertyDialogListAdapter dialogView = new PropertyDialogListAdapter(PropertiesActivity.this, property);
                    dialogBuilder.setAdapter(dialogView, null)
                            .setTitle(properties.get(position).getAddress()).create().show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // disable animations when leaving activity (intended for when Back button is pressed)
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    private void signOut() {
        // removes active user entry from sharedpref
        // todo: (possibly) Remember me feature?
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult (intent,0);
        finish();
    }

}