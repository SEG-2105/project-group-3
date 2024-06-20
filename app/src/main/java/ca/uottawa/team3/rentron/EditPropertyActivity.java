package ca.uottawa.team3.rentron;

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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FieldPath;

import java.util.ArrayList;
import java.util.Objects;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Properties.PropertyCreator;
import ca.uottawa.team3.rentron.Users.Invitations.InvitationHandler;
import ca.uottawa.team3.rentron.Users.PropertyMgr;

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
    PropertyMgr propertyMgrToAssign; // the property manager that will be assigned to this property (if applicable.)
    double commission; // commission of manager invite (if applicable.)
    String propertyDocId;
    String originalRent; // used for verifying if rent was changed
    String landlordEmail;

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
        landlordEmail = "";

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
                                landlordEmail = db_property.getLandlord();

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
                                originalRent = db_property.getRent();

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

                                propertyDocId = document.getId(); // get UUID of property to prevent any conflicts when editing/removing
                            }
                        } else {
                            Log.d("EditPropertyActivity:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        deleteProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming 'properties' is the name of your collection
                firestore.collection("properties")
                        .whereEqualTo(FieldPath.documentId(), propertyDocId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    // Assuming there is only one document with the given ID
                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                    String client = selectClient.getText().toString();
                                    if (client.equals("")) {
                                        // Delete the document if client is empty
                                        document.getReference().delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Property deleted successfully.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                                                        startActivityForResult(intent, 0);
                                                        finish();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed to delete the property.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // Show a toast if the property is occupied
                                        Toast.makeText(getApplicationContext(), "The property is occupied.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Show a toast if no property is found with the given address
                                    Toast.makeText(getApplicationContext(), "No property found with the given address.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        selectClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectClient.getText().toString().isEmpty()) {
                    selectClient.setText("Example Client");
                } else {
                    selectClient.setText("");
                }
            }
        });

        selectMgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPropertyMgrDialog(firestore);
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
        final String address = String.valueOf(propertyAddress.getText());
        final String type = String.valueOf(propertyType.getSelectedItem());
        final String unit = String.valueOf(propertyUnit.getText());
        final String floor = String.valueOf(propertyFloor.getText());
        final String numRoom = String.valueOf(propertyBedrooms.getText());
        final String numBathroom = String.valueOf(propertyBathrooms.getText());
        final String numFloor = String.valueOf(propertyNumFloors.getText());
        final String area = String.valueOf(propertyArea.getText());
        final String laundry = String.valueOf(propertyLaundry.getSelectedItem());
        final String numParkingSpot = String.valueOf(propertyParking.getText());
        final String rent = String.valueOf(propertyRent.getText());
        final String mgr = String.valueOf(selectMgr.getText());
        final String client = String.valueOf(selectClient.getText());
        final boolean heating = propertyHeating.isChecked();
        final boolean hydro = propertyHydro.isChecked();
        final boolean water = propertyWater.isChecked();

        if (fieldCheck(address, type, unit, floor, numRoom, numBathroom,
                numFloor, area, laundry, numParkingSpot, rent)) {
            if (!originalRent.equals(rent)) {
                if (!((client.isEmpty()) || (Objects.isNull(client)))) {
                    Toast.makeText(getApplicationContext(), "Cannot update rent if client assigned.", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateDoc(address, type, unit, floor, numRoom, numBathroom, numFloor, area, laundry, numParkingSpot, rent,
                            mgr, client, heating, hydro, water);

                    // invitation logic for now
                    PropertyMgr manager = new PropertyMgr("", "", mgr); // create blank mgr to simulate invitation system... will be expanded upon
                    InvitationHandler inviteHandler = new InvitationHandler(propertyDocId, manager, landlordEmail, commission);
                    inviteHandler.sendInviteToManager(); // dummy code, will be expanded on

                }
            }
            else {
                if ((!client.equals("")) && (mgr.equals(""))) {
                    Toast.makeText(getApplicationContext(), "Cannot invite client if no manager assigned.", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateDoc(address, type, unit, floor, numRoom, numBathroom, numFloor, area, laundry, numParkingSpot, rent,
                            mgr, client, heating, hydro, water);
                }
            }

        }
        else {
            Toast.makeText(getApplicationContext(), "Fields invalid.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDoc(String address, String type, String unit, String floor, String numRoom, String numBathroom,
                          String numFloor, String area, String laundry, String numParkingSpot, String rent, String mgr,
                          String client, boolean heating, boolean hydro, boolean water) {
        firestore.collection("properties")
                .whereEqualTo(FieldPath.documentId(), propertyDocId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Assuming there is only one document with the given ID
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            DocumentReference reference = document.getReference();
                            reference.update("address", address,
                                            "type", type,
                                            "unit", unit,
                                            "floor", floor,
                                            "numRoom", numRoom,
                                            "numBathroom", numBathroom,
                                            "numFloor", numFloor,
                                            "area", area,
                                            "laundry", laundry,
                                            "numParkingSpot", numParkingSpot,
                                            "rent", rent,
                                            "manager", mgr,
                                            "client", client,
                                            "heating", heating,
                                            "hydro", hydro,
                                            "water", water).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("EditPropertyActivity:", "Property successfully updated!");
                                            Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                                            startActivityForResult (intent,0);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("EditPropertyActivity:", "Error updating property", e);
                                        }
                                    });

                        } else {
                            // Show a toast if no property is found with the given address
                            Toast.makeText(getApplicationContext(), "No property found with the given address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClickDeleteProperty(View view) {
    }

    private void showPropertyMgrDialog(FirebaseFirestore db) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.register_property_selectmgr_dialog, null);
        dialogBuilder.setView(dialogView);
        final ListView propertyMgrListView = dialogView.findViewById(R.id.listViewPropertyMgrs);
        EditText commissionEditText = dialogView.findViewById(R.id.editTextCommissionNumber);
        commissionEditText.setText(Double.toString(commission));

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
                            PropertyMgrListAdapter mgrsAdapter = new PropertyMgrListAdapter(EditPropertyActivity.this, propertyMgrList);

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
                try {
                    commission = Double.parseDouble(commissionEditText.getText().toString());
                    Log.d("Commission:", Double.toString(commission));
                    if ((commission < 0) || (commission >= 100)) {
                        showDialogFailureToast();
                    }
                    else {
                        propertyMgrToAssign = propertyMgrList.get(i);
                        selectMgr.setText(propertyMgrToAssign.getFirstName() + " " + propertyMgrToAssign.getLastName());
                        b.dismiss();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(b.getContext(), "Invalid commission inputted.", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void showDialogFailureToast() {
        Toast.makeText(this, "Invalid commission inputted, needs to be between 0 and 99.", Toast.LENGTH_SHORT).show();
    }

    private boolean fieldCheck(
            String address, String type, String unit, String floor, String numRoom, String numBathroom,
            String numFloor, String area, String laundry, String numParkingSpot,
            String rent) {

        // field checking logic goes here...
        return !(address.isEmpty() || type.isEmpty() || ( type.equals("apartment") && floor.isEmpty()) ||( type.equals("apartment") && unit.isEmpty())|| numRoom.isEmpty() || numBathroom.isEmpty() || numFloor.isEmpty() || area.isEmpty() || laundry.isEmpty() || numParkingSpot.isEmpty() || rent.isEmpty());
    }

}