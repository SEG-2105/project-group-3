package ca.uottawa.team3.rentron;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ca.uottawa.team3.rentron.Users.PropertyMgr;
import ca.uottawa.team3.rentron.Users.Tickets.Invitation;

public class SelectPropertyMgrActivity extends AppCompatActivity {

    boolean isOccupied;
    ListView propertyMgrListView;
    Button unassign;
    FirebaseFirestore db;
    double commission;
    String property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_property_mgr);
        Toolbar topBar = findViewById(R.id.topBar);

        commission = 0.0; // initialize as 0

        db = FirebaseFirestore.getInstance();
        propertyMgrListView = findViewById(R.id.listViewPropertyMgrs);
        unassign = findViewById(R.id.btnUnassignManager);

        isOccupied = getIntent().getBooleanExtra("isOccupied", false);
        property = getIntent().getStringExtra("property");


        if (isOccupied) {
            unassign.setVisibility(View.GONE);
        } else {
            unassign.setVisibility(View.VISIBLE);
        }

        ArrayList<PropertyMgr> propertyMgrList = new ArrayList<PropertyMgr>();

        db.collection("users").whereEqualTo("role", "property-manager")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PropertyMgr propertyMgr = new PropertyMgr((String) document.get("firstname"), (String) document.get("lastname"),
                                        (String) document.get("email"), ((Long) document.get("avgRating")).doubleValue(), ((Long) document.get("numRatings")).intValue(), ((Long) document.get("numTicketsHandled")).intValue());
                                propertyMgrList.add(propertyMgr);
                            }
                            PropertyMgrListAdapter mgrsAdapter = new PropertyMgrListAdapter(SelectPropertyMgrActivity.this, propertyMgrList);

                            propertyMgrListView.setAdapter(mgrsAdapter);
                        } else {
                            Log.d("SelectPropertyMgrActivity:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        propertyMgrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                propMgrInvitationDialog(propertyMgrList.get(position));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        unassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to unassign the current property manager from this property?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: implement removal of manager from property
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
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

    private void showToastFromDialog(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void propMgrInvitationDialog(PropertyMgr propertyMgr) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_selectmgr_invitation_dialog, null);
        dialogBuilder.setView(dialogView);
        final TextView numPropsManaged = dialogView.findViewById(R.id.numPropsManaged);
        final TextView numPropsCurrentlyManaged = dialogView.findViewById(R.id.numPropsCurrentlyManaged);
        final TextView numTicketsHandled = dialogView.findViewById(R.id.numTicketsHandled);
        final TextView mgrAvgRatingDetails = dialogView.findViewById(R.id.mgrAvgRatingDetails);
        EditText commissionEditText = dialogView.findViewById(R.id.editTextCommissionNumber);
        commissionEditText.setText(Double.toString(commission));
        numPropsManaged.setText("Total Num. of Properties Managed: ");
        numPropsCurrentlyManaged.setText("Num. of Properties Currently Managed: ");
        numTicketsHandled.setText("Num. of Tickets Handled: " + propertyMgr.getNumTicketsHandled());
        mgrAvgRatingDetails.setText("Average Rating: " + propertyMgr.getAvgRating() + "/5");

        dialogBuilder.setTitle("Profile: " + propertyMgr.getFirstName() + " " + propertyMgr.getLastName())
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            commission = Double.parseDouble(commissionEditText.getText().toString());
                            Log.d("Commission:", Double.toString(commission));
                            if ((commission < 0) || (commission >= 100)) {
                                showToastFromDialog("Invalid commission inputted, needs to be between 0 and 99.");
                            }
                            else {
                                // TODO: invitation logic
                                dialog.dismiss();
                            }
                        }
                        catch (Exception e) {
                            showToastFromDialog("Invalid commission inputted, needs to be between 0 and 99.");
                        }
                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }


}