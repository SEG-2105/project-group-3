package ca.uottawa.team3.rentron;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private String firstName, lastName, role;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        byte[] active = Base64.decode(pref.getString("active", ""), Base64.DEFAULT);

        String activeEmail = "";
        try {
            activeEmail = new String(active, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        TextView welcomeText = findViewById(R.id.welcomeTextView);
        TextView roleText = findViewById(R.id.roleTextView);

        db.collection("users").whereEqualTo("email", activeEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult().isEmpty()) {
                    Log.d("WELCOME:", "FAILURE TO IDENTIFY USER");
                    Toast.makeText(getApplicationContext(), "Could not find active user???", Toast.LENGTH_LONG).show();
                }
                else {
                    DocumentSnapshot user = task.getResult().getDocuments().get(0);
                    firstName = (String)user.get("firstname");
                    lastName = (String)user.get("lastname");
                    role = (String)user.get("role");
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