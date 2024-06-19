package ca.uottawa.team3.rentron;

import ca.uottawa.team3.rentron.Users.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Objects;

public class RegisterPropertyActivity extends AppCompatActivity {

    Button selectMgr, register;
    FirebaseFirestore firestore;
    List<PropertyMgr> propertyMgrList;
    PropertyMgr propertyMgr; // the property manager that will be assigned to this property (if applicable.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_property);

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

        selectMgr = (Button)findViewById(R.id.propertyManager);
        register = (Button)findViewById(R.id.btnRegisterProperty);

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
                fieldCheck();
                Property property; // = new Property(...args...);
                // add property registration logic here...

                // basic invitation logic
                String propertyId = ""; // this would be the unique Firebase document ID of the property
                if (!Objects.isNull(propertyMgr)) {
                    propertyMgr.addInvitation(propertyId);
                }
                else {
                    // do nothing...?
                }
                // clear selected PropertyMgr's invitation list since, for now, all invitations are automatically accepted
                propertyMgr.clearInvitations();

                // ending logic (subject to change)
                Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                startActivityForResult(intent, 0);
                finish();
            }
        });
    }

    private void fieldCheck() {
        // field checking logic goes here...
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