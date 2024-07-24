package ca.uottawa.team3.rentron;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.uottawa.team3.rentron.Users.Tickets.Ticket;

public class PropertyManagementTicketsActivity extends AppCompatActivity {

    // state variables when switching between details/tickets
    String property, type, landlord, tenant, rooms, bathrooms, laundry, parkingSpots, numOfFloors, floorNum, unit, rent;
    Boolean hydro, heating, water;

    FirebaseFirestore db;
    List<Ticket> activeTickets = new ArrayList<>();
    List<Ticket> closedTickets = new ArrayList<>();
    List<String> activeTicketsName = new ArrayList<>();
    List<String> closedTicketsName = new ArrayList<>();

    ListView listViewActiveTickets, listViewClosedTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_mgmt_tickets);
        property = getIntent().getStringExtra("property");
        type = getIntent().getStringExtra("type");
        landlord = getIntent().getStringExtra("landlord");
        tenant = getIntent().getStringExtra("tenant");
        rooms = getIntent().getStringExtra("rooms");
        bathrooms = getIntent().getStringExtra("bathrooms");
        laundry = getIntent().getStringExtra("laundry");
        parkingSpots = getIntent().getStringExtra("parkingSpots");
        numOfFloors = getIntent().getStringExtra("numOfFloors");
        floorNum = getIntent().getStringExtra("floorNum");
        unit = getIntent().getStringExtra("unit");
        rent = getIntent().getStringExtra("rent");
        hydro = getIntent().getBooleanExtra("hydro", false);
        heating = getIntent().getBooleanExtra("heating", false);
        water = getIntent().getBooleanExtra("water", false);

        db = FirebaseFirestore.getInstance();

        // initialize UI

        listViewActiveTickets = findViewById(R.id.listViewActiveTickets);
        listViewClosedTickets = findViewById(R.id.listViewClosedTickets);

        db.collection("tickets")
                .whereEqualTo("property", property)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            Ticket db_ticket = new Ticket(
                                    (String)document.get("idClient"),
                                    (String)document.get("idPropertyMgr"),
                                    (String)document.get("property"),
                                    (String)document.get("type"),
                                    (String)document.get("messageCreation"),
                                    ((Long)document.get("urgency")).intValue(),
                                    (String)document.get("name"),
                                    ((Long)document.get("Event")).intValue(),
                                    (String)document.get("rating")
                            );

                            db_ticket.setEvent(((Long)document.get("Event")).intValue());

                            if (document.get("Status").equals("Closed")) {
                                closedTickets.add(db_ticket);
                                closedTicketsName.add(db_ticket.getName());
                            } else {
                                activeTickets.add(db_ticket);
                                activeTicketsName.add(db_ticket.getName());
                            }
                        }

                        ArrayAdapter<String> arrayAdapterActive = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activeTicketsName);
                        ArrayAdapter<String> arrayAdapterClosed = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, closedTicketsName);

                        listViewActiveTickets.setAdapter(arrayAdapterActive);
                        listViewClosedTickets.setAdapter(arrayAdapterClosed);
                    } else {
                        Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        listViewActiveTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (activeTickets.get(i).getEvent() == 1) {
                    showAcceptDialog(activeTickets.get(i));
                } else if (activeTickets.get(i).getEvent() == 2) {
                    showResolveDialog(activeTickets.get(i));
                }
            }
        });

        listViewClosedTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                builder.setMessage(closedTickets.get(i).getText())
                        .setTitle(closedTicketsName.get(i))
                        .create().show();
            }
        });

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);
        getSupportActionBar().setTitle(property);

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PropertiesActivity.class);
                startActivityForResult (intent,0);
                overridePendingTransition(0,0);
                finish();
            }
        });

        // Bottom nav view/ViewCompat initialization

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.tickets);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.tickets)
                {
                    return true;
                }
                else if (item.getItemId() == R.id.details) {
                    moveToDetails();
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

    private void moveToDetails() {
        Intent intent = new Intent(getApplicationContext(), PropertyManagementDetailsActivity.class);
        intent.putExtra("property", property);
        intent.putExtra("type", type);
        intent.putExtra("landlord", landlord);
        intent.putExtra("tenant", tenant);
        intent.putExtra("rooms", rooms);
        intent.putExtra("bathrooms", bathrooms);
        intent.putExtra("laundry", laundry);
        intent.putExtra("parkingSpots", parkingSpots);
        intent.putExtra("numOfFloors", numOfFloors);
        intent.putExtra("floorNum", floorNum);
        intent.putExtra("unit", unit);
        intent.putExtra("rent", rent);
        intent.putExtra("hydro", hydro);
        intent.putExtra("heating", heating);
        intent.putExtra("water", water);
        startActivityForResult(intent, 0);
        overridePendingTransition(0,0);
    }

    private void updateTicketDB(Ticket ticket) {
        db.collection("tickets").whereEqualTo("name",ticket.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        DocumentReference ref = doc.getReference();
                        ref.update("Event", ticket.getEvent());
                        ref.update("Status", ticket.getStatus());
                        ref.update("messageCreation", ticket.getText());
                    }
                });
        moveToDetails();
    }

    private void showResolveDialog(Ticket ticket) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.ticket_dialog_resolve, null);
        builder.setView(dialogView);

        final EditText editTextMessage = dialogView.findViewById(R.id.editTextMessage);
        final TextView textTicketHistory = dialogView.findViewById(R.id.textTicketHistory);
        textTicketHistory.setText(ticket.getText());

        Button buttonSubmit = dialogView.findViewById(R.id.buttonResolve);

        final AlertDialog dialog = builder.create();
        dialog.show();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMessage = editTextMessage.getText().toString();
                String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                ticket.addMessage("Resolved at " + dateTime + "\n" + inputMessage);
                ticket.setEvent(3);
                updateTicketDB(ticket);

                //Toast.makeText(PropertyManagementTicketsActivity.this, "Rating: " + rating + "\nMessage: " + message, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showAcceptDialog(Ticket ticket) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.ticket_dialog_choose_action, null);
        builder.setView(dialogView);

        // Get the dialog elements
        Button acceptButton = dialogView.findViewById(R.id.button_accept);
        Button declineButton = dialogView.findViewById(R.id.button_decline);
        Button sendButton = dialogView.findViewById(R.id.button_send);
        TextView ticketHistoryView = dialogView.findViewById(R.id.textTicketHistory);
        final EditText inputMessage = dialogView.findViewById(R.id.input_message);
        final Button btnSend = dialogView.findViewById(R.id.button_send);

        ticketHistoryView.setText(ticket.getText());

        final AlertDialog dialog = builder.create();
        dialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                ticket.addMessage("Accepted at " + dateTime);
                ticket.setEvent(2);
                updateTicketDB(ticket);
                dialog.dismiss();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMessage.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                acceptButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMessage.getVisibility() == View.VISIBLE && !inputMessage.getText().toString().isEmpty()) {
                    String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    String message = inputMessage.getText().toString();
                    ticket.addMessage("Rejected at " + dateTime + "\n" + message);
                    ticket.setEvent(3);
                    updateTicketDB(ticket);
                    dialog.dismiss();
                } else {
                    Toast.makeText(PropertyManagementTicketsActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}