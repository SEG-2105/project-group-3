package ca.uottawa.team3.rentron;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Users.Tickets.Courier;
import ca.uottawa.team3.rentron.Users.Tickets.Request;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private String firstName, lastName, role, email;
    private List<Request> activeRequests = new ArrayList<>();
    private List<Request> rejectedRequests = new ArrayList<>();
    private ListView activeApplications, rejectedApplications;
    //private RequestListAdapter adapter; // used when refreshing list
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        EdgeToEdge.enable(this);
        byte[] active = Base64.decode(pref.getString("active", ""), Base64.DEFAULT);
        byte[] active1 = Base64.decode(pref.getString("activeRole", ""), Base64.DEFAULT);;

        String activeEmail = "";
        String activeRole = "";
        try {
            activeEmail = new String(active, "UTF-8");
            activeRole = new String(active1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        role = activeRole;
        email = activeEmail;


        if (activeRole.equals("landlord")) {
            setContentView(R.layout.activity_welcome_landlord);

            TextView welcomeText = findViewById(R.id.welcomeTextView);

            db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if(task.getResult().isEmpty()) {
                        Log.d("WELCOME:", "FAILURE TO IDENTIFY USER");
                        Toast.makeText(getApplicationContext(), "Could not find active user???", Toast.LENGTH_LONG).show();
                    }
                    else {
                        DocumentSnapshot user = task.getResult().getDocuments().get(0);
                        firstName = (String)user.get("firstname");
                        lastName = (String)user.get("lastname");
                        //role = (String)user.get("role");
                        String welcome = ("Welcome, " + firstName + " " + lastName + "!");
                        welcomeText.setText(welcome);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            setContentView(R.layout.activity_welcome);

            TextView welcomeText = findViewById(R.id.welcomeTextView);
            TextView roleText = findViewById(R.id.applicationsTextView);

            db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if(task.getResult().isEmpty()) {
                        Log.d("WELCOME:", "FAILURE TO IDENTIFY USER");
                        Toast.makeText(getApplicationContext(), "Could not find active user???", Toast.LENGTH_LONG).show();
                    }
                    else {
                        DocumentSnapshot user = task.getResult().getDocuments().get(0);
                        firstName = (String)user.get("firstname");
                        lastName = (String)user.get("lastname");
                        //role = (String)user.get("role");
                        //Toast.makeText(getApplicationContext(), "Got data!", Toast.LENGTH_LONG).show();
                        String welcome = ("Welcome, " + firstName + " " + lastName + "!");
                        String yourRole = ("Your role is: " + role);
                        welcomeText.setText(welcome);
                        roleText.setText(yourRole);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.welcome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.properties)
                {
                    Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(0,0);
                    return true;
                }
                else if (item.getItemId() == R.id.welcome) {
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
        bottomNavigationView.setSelectedItemId(R.id.welcome);

        if(role.equals("landlord")) {
            //Button refresh = findViewById(R.id.refreshButton);
            // Clear the previous list
            activeRequests.clear();
            rejectedRequests.clear();
            db = FirebaseFirestore.getInstance();
            //Toast.makeText(getApplicationContext(), "Creating application list.", Toast.LENGTH_SHORT).show();
            activeApplications = findViewById(R.id.applicationListView);
            db.collection("requests")
                    .whereEqualTo("idLandlord", email)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("WelcomeActivity:", document.getId() + " => " + document.get("property"));
                                Request db_request = new Request((String) document.get("idClient"), (String) document.get("idLandlord"), (String) document.get("property"));
                                if ((Boolean) document.get("rejected")) {
                                    rejectedRequests.add(db_request);
                                } else {
                                    activeRequests.add(db_request);
                                }
                            }
                            // Create the landlord specific adapter
                            RequestListAdapter activeRequestAdapter = new RequestListAdapter(WelcomeActivity.this, activeRequests);

                            // Attach the adapter to the list view
                            activeApplications.setAdapter(activeRequestAdapter);
//
//                            RequestListAdapter rejectedRequestAdapter = new RequestListAdapter(WelcomeActivity.this, rejectedRequests);
//
//                            // Attach the adapter to the list view
//                            rejectedApplications.setAdapter(rejectedRequestAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "An error has occurred while fetching requests.", Toast.LENGTH_SHORT).show();
                        }
                    });

            createApplicationDialogBox();
//            refresh.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
////                    finish();
////                    startActivity(intent);
//                    activeRequestAdapter.notifyDataSetChanged();
//                    rejectedRequestAdapter.notifyDataSetChanged();
//                }
//            });
        }
    }

    // disable animations when leaving activity (intended for when Back button is pressed)
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
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

    private void createApplicationDialogBox() {
        //Box that appears for landlord when clicking on a request
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //Viewing full details of a property only; available to client
        activeApplications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Request> request = new ArrayList<>();
                request.add(activeRequests.get(position));
                Request req = activeRequests.get(position);
                RequestListAdapter dialogView = new RequestListAdapter(WelcomeActivity.this, request);
                dialogBuilder.setAdapter(dialogView, null)
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //edit property to add client
                                db.collection("properties")
                                        .whereEqualTo("address", req.getProperty())
                                        .whereEqualTo("landlord", req.getLandlord())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    // Assuming there is only one document with the given address
                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                    DocumentReference reference = document.getReference();
                                                    reference.update("client",req.getClient()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("WelcomeActivity:", "Client successfully added to property!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("WelcomeActivity:", "Error adding client", e);
                                                                }
                                                            });

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No property found.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                db.collection("requests")
                                        .whereEqualTo("idClient", req.getClient())
                                        .whereEqualTo("rejected", false)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        Log.d("WelcomeActivity:", doc.getId() + " => " + doc.get("property"));
                                                        doc.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("WelcomeActivity:", "Requests rejected!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w("WelcomeActivity:", "Error rejecting client", e);
                                                                    }
                                                                });
                                                    }

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No request found.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                db.collection("requests")
                                        .whereEqualTo("property", req.getProperty())
                                        .whereEqualTo("idLandlord", req.getLandlord())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        Log.d("WelcomeActivity:", doc.getId() + " => " + doc.get("property"));
                                                        doc.getReference().update("rejected",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("WelcomeActivity:", "Requests rejected!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w("WelcomeActivity:", "Error rejecting client", e);
                                                                    }
                                                                });
                                                    }

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No request found.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                dialog.dismiss();
                            }
                        }).setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("requests")
                                        .whereEqualTo("idLandlord", req.getLandlord())
                                        .whereEqualTo("idClient", req.getClient())
                                        .whereEqualTo("property", req.getProperty())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        Log.d("WelcomeActivity:", doc.getId() + " => " + doc.get("property"));
                                                        doc.getReference().update("rejected",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("WelcomeActivity:", "Requests rejected!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w("WelcomeActivity:", "Error rejecting client", e);
                                                                    }
                                                                });
                                                    }

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No request found.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                dialog.dismiss();
                            }
                        })
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                                        startActivityForResult(intent, 0);
                                        overridePendingTransition(0,0);
                                    }
                                })
                        .setTitle(activeRequests.get(position).getProperty()).create().show();
            }
        });

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