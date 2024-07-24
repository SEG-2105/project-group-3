package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import ca.uottawa.team3.rentron.Users.Tickets.Ticket;

public class PropertyManagementTicketsActivity extends AppCompatActivity {

    String property;
    List<Ticket> activeTickets = new ArrayList<>();
    List<Ticket> closedTickets = new ArrayList<>();
    ListView listViewActiveTickets, listViewClosedTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_mgmt_tickets);
        property = getIntent().getStringExtra("property");
        //... add other extras if needed

        // initialize UI

        listViewActiveTickets = findViewById(R.id.listViewActiveTickets);
        listViewClosedTickets = findViewById(R.id.listViewClosedTickets);

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
                    Intent intent = new Intent(getApplicationContext(), PropertyManagementDetailsActivity.class);
                    intent.putExtra("property", property);
                    //... add fields as needed
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
}