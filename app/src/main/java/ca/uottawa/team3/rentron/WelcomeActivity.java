package ca.uottawa.team3.rentron;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        TextView welcomeText = findViewById(R.id.welcomeTextView);
        TextView roleText = findViewById(R.id.roleTextView);
        welcomeText.setText("Welcome, " + pref.getString("firstName", "") + " " + pref.getString("lastName", "") + "!");
        roleText.setText("Your role is: " + pref.getString("role",""));

        Toolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options_menu, menu);
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
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult (intent,0);
    }
}