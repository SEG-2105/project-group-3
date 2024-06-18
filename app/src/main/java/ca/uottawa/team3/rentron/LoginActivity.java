package ca.uottawa.team3.rentron;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.DestroyFailedException;

import ca.uottawa.team3.rentron.Users.Hashing;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // remove active user data upon return to login page
        SharedPreferences pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        Log.d("LoginActivity", "onResume(): cleared data successfully");
    }

    public void onSignInClick(View view) {
        EditText emailField = (EditText)findViewById(R.id.editTextUsername);
        EditText passwordField = (EditText)findViewById(R.id.editTextPassword);
        String email = emailField.getText().toString().toLowerCase();
        String password = passwordField.getText().toString();

        auth(email, password);

    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult (intent,0);
    }

    private void auth(String email, String passwordPlain) {
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult().isEmpty()) {
                    Log.d("AUTH:", "FAILURE");
                    Toast.makeText(getApplicationContext(), "Login failure, invalid username and/or password?", Toast.LENGTH_LONG).show();
                }
                else {
                    DocumentSnapshot user = task.getResult().getDocuments().get(0);
                    Hashing hasher = new Hashing(getApplicationContext());
                    byte[] salt = Base64.decode((String)user.get("salt"), Base64.DEFAULT);
                    byte[] encodedPass = Base64.decode((String)user.get("password"), Base64.DEFAULT);
                    SecretKey password = new SecretKeySpec(encodedPass, 0, encodedPass.length, "HmacSHA256");
                    //Log.d("AUTH:", "SALT = " + salt.toString() + " , HASH = " + encodedPass.toString());
                    if (hasher.verifyPassword(salt, passwordPlain, password)) {
                        Log.d("AUTH:", "SUCCESSFUL");
                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();

                        keepUserInfo((String)user.get("email"), (String)user.get("role"));
                        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        finish();
                    }
                    else {
                        Log.d("AUTH:", "FAILURE");
                        Toast.makeText(getApplicationContext(), "Login failure, invalid username and/or password?", Toast.LENGTH_LONG).show(); // leave this more ambiguous later
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // remembers active user for context
    private void keepUserInfo(String email, String role) {
        SharedPreferences pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String encodedEmail = "";
        String encodedRole = "";
        try {
            encodedEmail = Base64.encodeToString(email.getBytes("UTF-8"), Base64.DEFAULT);
            encodedRole = Base64.encodeToString(role.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        editor.putString("active", encodedEmail);
        editor.putString("activeRole", encodedRole);
        editor.commit();
    }

}