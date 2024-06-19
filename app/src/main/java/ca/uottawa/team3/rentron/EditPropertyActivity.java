package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import ca.uottawa.team3.rentron.Properties.Property;

public class EditPropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText
            propertyFloor,
            propertyUnit,
            propertyNumFloors,
            propertyBedrooms,
            propertyBathrooms,
            propertyArea,
            propertyParking,
            propertyRent,
            propertyAddress;

    private CheckBox propertyHydro, propertyHeating, propertyWater;

    private Spinner propertyType, propertyLaundry;
    private TextView propertyFloorLabel, propertyUnitLabel, propertyNumFloorsLabel;


    Button selectMgr, edit, selectClient, deleteProperty;
    ArrayAdapter<CharSequence> propertyTypeAdapter, propertyLaundryAdapter;

    FirebaseFirestore firestore;
    Property propertyToEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_property);

        propertyAddress = findViewById(R.id.propertyAddress);
        propertyFloor = findViewById(R.id.propertyFloor);
        propertyBedrooms = findViewById(R.id.propertyBedrooms);
        propertyBathrooms = findViewById(R.id.propertyBathrooms);
        propertyNumFloors = findViewById(R.id.propertyNumFloors);
        propertyArea = findViewById(R.id.propertyArea);
        propertyParking = findViewById(R.id.propertyParking);
        propertyRent = findViewById(R.id.propertyRent);
        propertyHydro = findViewById(R.id.propertyHydro);
        propertyHeating = findViewById(R.id.propertyHeating);
        propertyWater = findViewById(R.id.propertyWater);
        propertyUnit = findViewById(R.id.propertyUnit);
        propertyType = findViewById(R.id.propertyType);
        propertyLaundry = findViewById(R.id.propertyLaundry);

        propertyType.setOnItemSelectedListener(this);
        propertyLaundry.setOnItemSelectedListener(this);

        selectMgr = (Button)findViewById(R.id.propertyManager);
        selectClient = (Button)findViewById(R.id.propertyClient);

        edit = (Button)findViewById(R.id.btnEditProperty);
        deleteProperty = (Button)findViewById(R.id.btnDeleteProperty);

        propertyFloorLabel = findViewById(R.id.propertyFloorLabel);
        propertyUnitLabel = findViewById(R.id.propertyUnitLabel);
        propertyNumFloorsLabel = findViewById(R.id.propertyNumFloorsLabel);

        String propertyAddressDB = getIntent().getStringExtra("property");
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("properties")
                .whereEqualTo("address", propertyAddressDB)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Property db_property = new Property((String)document.get("address"), (String)document.get("type"), (String)document.get("unit"), (String)document.get("floor"),
                                        (String)document.get("numRoom"), (String)document.get("numBathroom"), (String)document.get("numFloor"), (String)document.get("area"),
                                        (String)document.get("laundry"), (String)document.get("numParkingSpot"), (String)document.get("rent"),
                                        (boolean)document.get("heating"), (boolean)document.get("hydro"), (boolean)document.get("water"),
                                        (String)document.get("landlord"), (String)document.get("manager"), (String)document.get("client"));


                                // Populate the views with db_property data
                                propertyAddress.setText(db_property.getAddress());
                                propertyNumFloors.setText(db_property.getNumFloor());
                                propertyBedrooms.setText(db_property.getNumRoom());
                                propertyBathrooms.setText(db_property.getNumBathroom());
                                propertyArea.setText(db_property.getArea());
                                propertyParking.setText(db_property.getNumParkingSpot());
                                propertyFloor.setText(db_property.getFloor());
                                propertyUnit.setText(db_property.getUnit());
                                propertyRent.setText(db_property.getRent());

                                // Populate Spinners with values
                                ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(EditPropertyActivity.this, R.array.propertyTypeArray, android.R.layout.simple_spinner_item);
                                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                propertyType.setAdapter(typeAdapter);

                                int typePosition = typeAdapter.getPosition(db_property.getType());
                                propertyType.setSelection(typePosition);

                                ArrayAdapter<CharSequence> laundryAdapter = ArrayAdapter.createFromResource(EditPropertyActivity.this, R.array.propertyLaundryArray, android.R.layout.simple_spinner_item);
                                laundryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                propertyLaundry.setAdapter(laundryAdapter);

                                int laundryPosition = laundryAdapter.getPosition(db_property.getLaundry());
                                propertyLaundry.setSelection(laundryPosition);

                                // Set CheckBoxes
                                propertyHydro.setChecked(db_property.getHydro());
                                propertyHeating.setChecked(db_property.getHeating());
                                propertyWater.setChecked(db_property.getWater());

                                // Set Buttons
                                selectMgr.setText(db_property.getManager());
                                selectClient.setText(db_property.getClient());
                            }
                        } else {
                            Log.d("EditPropertyActivity:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        deleteProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("properties")
                        .whereEqualTo("address", propertyAddressDB)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        firestore.collection("properties").document(document.getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Document successfully deleted
                                                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // An error occurred
                                                        Log.w("Firestore", "Error deleting document", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        if (parent.getId() == R.id.propertyType) {
            applyPropertyTypeRules(selectedItem);
        }
    }

    private void applyPropertyTypeRules(String selectedItem) {
        if (selectedItem.equals("Apartment")) {
            propertyFloor.setEnabled(true);
            propertyUnit.setEnabled(true);
            propertyNumFloors.setEnabled(false);
            removeStrikethrough(propertyFloorLabel);
            removeStrikethrough(propertyUnitLabel);
            applyStrikethrough(propertyNumFloorsLabel);
            propertyNumFloors.setText("1");
        } else {
            propertyFloor.setEnabled(false);
            propertyUnit.setEnabled(false);
            propertyNumFloors.setEnabled(true);
            applyStrikethrough(propertyFloorLabel);
            applyStrikethrough(propertyUnitLabel);
            removeStrikethrough(propertyNumFloorsLabel);
            propertyNumFloors.setText("");
            propertyFloor.setText("");
            propertyUnit.setText("");
        }
    }
    private void applyStrikethrough(TextView textView) {
        String text = textView.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), 0);
        textView.setText(spannableString);
    }

    private void removeStrikethrough(TextView textView) {
        textView.setText(textView.getText().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        // Required by the abstract class
    }

    public void onClickEditProperty(View view) {
    }

    public void onClickDeleteProperty(View view) {
    }
}