package ca.uottawa.team3.rentron;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
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

// DEBUG CODE (FEEL FREE TO DELETE):
//        Map<String, Object> user = new HashMap<>();
//        user.put("firstname", "Artur");
//        user.put("lastname", "Womp");
//        user.put("description", "Likes Cheese");
//        firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public void onSignInClick(View view) {
        EditText emailField = (EditText)findViewById(R.id.editTextUsername);
        EditText passwordField = (EditText)findViewById(R.id.editTextPassword);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (AuthManager.auth(email, password)) {
            Log.d("AUTH:", "SUCCESSFUL");
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivityForResult (intent,0);
        }
        else {
            Log.d("AUTH:", "FAILURE");
            Toast.makeText(getApplicationContext(), "Login failure, invalid username and/or password?", Toast.LENGTH_LONG).show();
        }
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult (intent,0);
    }

    protected static class AuthManager {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        /* CAUTION: (copied from User.java)
        /  Current password implementation is UNSAFE, passwords are passed through as Strings. (this applies to all User subclasses as well)
        /  We need to find a safer method to store passwords (some type of hash?)
        */
        public static boolean auth(String email, String password) {
            // leaving implementation for later...
            return false;
        }

        public static boolean doesExist(User user) {
            // leaving implementation for later...
            return false;
        }
    }
}