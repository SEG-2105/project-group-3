package ca.uottawa.team3.rentron;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //private FirebaseAuth mAuth;
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
        String email = emailField.getText().toString().toLowerCase();
        String password = passwordField.getText().toString();

        auth(email, password);

    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult (intent,0);
    }

    private void auth(String email, String password) {
        db.collection("users").whereEqualTo("email", email).whereEqualTo("password", password).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult().isEmpty()) {
                    Log.d("AUTH:", "FAILURE");
                    Toast.makeText(getApplicationContext(), "Login failure, invalid username and/or password?", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("AUTH:", "SUCCESSFUL");
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    DocumentSnapshot user = task.getResult().getDocuments().get(0);

                    keepUserInfo((String)user.get("firstname"), (String)user.get("lastname"), (String)user.get("role"));
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // may not need if FirebaseAuth implemented
    private void keepUserInfo(String firstName, String lastName, String role) {
        SharedPreferences pref = getSharedPreferences("activeUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("role", role);
        editor.commit();
    }
}