package ca.uottawa.team3.rentron;

import ca.uottawa.team3.rentron.Users.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RegisterPropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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


    Button selectMgr, register;
    FirebaseFirestore firestore;
    List<PropertyMgr> propertyMgrList;
    PropertyMgr propertyMgr; // the property manager that will be assigned to this property (if applicable.)
    ArrayAdapter<CharSequence> propertyTypeAdapter, propertyLaundryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_property);

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
        register = (Button)findViewById(R.id.btnRegisterProperty);

        propertyFloorLabel = findViewById(R.id.propertyFloorLabel);
        propertyUnitLabel = findViewById(R.id.propertyUnitLabel);
        propertyNumFloorsLabel = findViewById(R.id.propertyNumFloorsLabel);

        // =============== Populating the Spinners ===============
        // Create an ArrayAdapter using the string array and a default spinner layout
        // The array can be found under res/values/array.xml
        propertyTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.propertyTypeArray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        propertyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        propertyType.setAdapter(propertyTypeAdapter);

        // Populate the laundry spinner the same way but use it's array
        propertyLaundryAdapter = ArrayAdapter.createFromResource(this,
                R.array.propertyLaundryArray, android.R.layout.simple_spinner_item);
        propertyLaundryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyLaundry.setAdapter(propertyLaundryAdapter);

        propertyType.setOnItemSelectedListener(this);
        propertyLaundry.setOnItemSelectedListener(this);

        // Use the property object as needed
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

    // set listener for PropertyMgr selector button (to open new register_property_selectmgr dialog)
    @Override
    public void onStart() {
        super.onStart();

        firestore = FirebaseFirestore.getInstance();

        selectMgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPropertyMgrDialog(firestore);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = propertyAddress.getText().toString();
                String type = propertyType.getSelectedItem().toString();
                String floor = propertyFloor.getText().toString();
                String numRoom = propertyBedrooms.getText().toString();
                String numBathroom = propertyBathrooms.getText().toString();
                String numFloor = propertyNumFloors.getText().toString();
                String area = propertyArea.getText().toString();
                String laundry = propertyLaundry.getSelectedItem().toString();
                String numParkingSpot = propertyParking.getText().toString();
                String rent = propertyRent.getText().toString();

                // Compile utilities into a comma-separated string
                StringBuilder utilitiesBuilder = new StringBuilder();
                if (propertyHydro.isChecked()) utilitiesBuilder.append("Hydro,");
                if (propertyHeating.isChecked()) utilitiesBuilder.append("Heating,");
                if (propertyWater.isChecked()) utilitiesBuilder.append("Water,");
                String utilities = utilitiesBuilder.toString();

                String landlord = "SomeLandlord";  // Replace with actual landlord value
                String manager = selectMgr.getText().toString();
                String client = register.getText().toString();

                if (fieldCheck(
                        address, type, floor, numRoom, numBathroom,
                        numFloor, area, laundry, numParkingSpot,
                        rent, utilities, landlord, manager, client)
                ) {
//                    Property property; // = new Property(...args...);
//                    PropertyMgr manager; // = new PropertyMgr(...args...);
//                    Landlord landlord; // = new Landlord(...args...);

                    Property property = new Property(
                            address,
                            type,
                            floor,
                            numRoom,
                            numBathroom,
                            numFloor,
                            area,
                            laundry,
                            numParkingSpot,
                            rent,
                            utilities,
                            landlord,
                            manager,
                            client
                    );

                    // add property registration logic here...

                    // invitation logic
                    //InvitationHandler inviteHandler = new InvitationHandler(property, manager);
                    //inviteHandler.sendInviteToManager();

                    // append to invitation logic something like:
                    /*
                        if(!Objects.isNull(manager)) {
                            ...manually add manager to property's Firebase document... (since we are assuming invites are automatically accepted)
                        }
                     */


                    // ending logic (subject to change)
                    Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                    startActivityForResult(intent, 0);
                    finish();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
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

    private boolean fieldCheck(
            String address, String type, String floor, String numRoom, String numBathroom,
            String numFloor, String area, String laundry, String numParkingSpot,
            String rent, String utilities, String landlord, String manager, String client) {

        // field checking logic goes here...
        return true;
    }

    private void showPropertyMgrDialog(FirebaseFirestore db) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.register_property_selectmgr_dialog, null);
        dialogBuilder.setView(dialogView);
        final ListView propertyMgrListView = dialogView.findViewById(R.id.listViewPropertyMgrs);

        ArrayList<PropertyMgr> propertyMgrList = new ArrayList<PropertyMgr>();

        db.collection("users").whereEqualTo("role", "property-manager")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PropertyMgr propertyMgr = new PropertyMgr((String) document.get("firstname"), (String) document.get("lastname"),
                                        (String) document.get("email"));
                                propertyMgrList.add(propertyMgr);
                            }
                            PropertyMgrListAdapter mgrsAdapter = new PropertyMgrListAdapter(RegisterPropertyActivity.this, propertyMgrList);

                            propertyMgrListView.setAdapter(mgrsAdapter);
                        } else {
                            Log.d("showPropertyMgrDialog:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        dialogBuilder.setTitle("Property Managers:");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        propertyMgrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                propertyMgr = propertyMgrList.get(i);
                selectMgr.setText(propertyMgr.getFirstName() + " " + propertyMgr.getLastName());
                b.dismiss();
            }
        });
    }
}