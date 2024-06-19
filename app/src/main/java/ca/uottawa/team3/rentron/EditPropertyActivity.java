package ca.uottawa.team3.rentron;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import ca.uottawa.team3.rentron.Properties.Property;

public class EditPropertyActivity extends AppCompatActivity {
    private EditText
            propertyFloor,
            propertyUnit,
            propertyNumFloors,
            propertyAddress,
            propertyBedrooms,
            propertyBathrooms,
            propertyArea,
            propertyParking,
            propertyRent;

    private CheckBox propertyHydro, propertyHeating, propertyWater;

    private Spinner propertyType, propertyLaundry;
    private TextView propertyFloorLabel, propertyUnitLabel, propertyNumFloorsLabel;


    Button selectMgr, edit, selectClient;
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

        selectMgr = (Button)findViewById(R.id.propertyManager);
        selectClient = (Button)findViewById(R.id.propertyClient);

        edit = (Button)findViewById(R.id.btnEditProperty);

        propertyFloorLabel = findViewById(R.id.propertyFloorLabel);
        propertyUnitLabel = findViewById(R.id.propertyUnitLabel);
        propertyNumFloorsLabel = findViewById(R.id.propertyNumFloorsLabel);

        String propertyAddress = getIntent().getStringExtra("property");
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("properties")
                .whereEqualTo("address", propertyAddress)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Property db_property = new Property((String)document.get("address"), (String)document.get("type"), (String)document.get("floor"),
                                        (String)document.get("numRoom"), (String)document.get("numBathroom"), (String)document.get("numFloor"), (String)document.get("area"),
                                        (String)document.get("laundry"), (String)document.get("numParkingSpot"), (String)document.get("rent"),
                                        (boolean)document.get("heating"), (boolean)document.get("water"), (boolean)document.get("hydro"),
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
                                ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.property_types, android.R.layout.simple_spinner_item);
                                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                propertyType.setAdapter(typeAdapter);
                                if (db_property.getType() != null) {
                                    int typePosition = typeAdapter.getPosition(db_property.getType());
                                    propertyType.setSelection(typePosition);
                                }

                                ArrayAdapter<CharSequence> laundryAdapter = ArrayAdapter.createFromResource(this, R.array.laundry_options, android.R.layout.simple_spinner_item);
                                laundryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                propertyLaundry.setAdapter(laundryAdapter);
                                if (db_property.getLaundry() != null) {
                                    int laundryPosition = laundryAdapter.getPosition(db_property.getLaundry());
                                    propertyLaundry.setSelection(laundryPosition);
                                }

                                // Set CheckBoxes
                                propertyHydro.setChecked(Objects.equals(db_property.getHydro(), "true"));
                                propertyHeating.setChecked(Objects.equals(db_property.getHeating(), "true"));
                                propertyWater.setChecked(Objects.equals(db_property.getWater(), "true"));

                                // Set Buttons
                                selectMgr.setText(db_property.getManager());
                                selectClient.setText(db_property.getClient());
                            }
                            }
                        } else {
                            Log.d("EditPropertyActivity:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}