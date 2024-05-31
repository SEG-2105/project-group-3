package ca.uottawa.team3.rentron;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthManager extends Application {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    /* CAUTION: (copied from User.java)
    /  Current password implementation is UNSAFE, passwords are passed through as Strings. (this applies to all User subclasses as well)
    /  We need to find a safer method to store passwords (some type of hash?)
    */
    public static boolean auth(String email, String password) {
        // leaving implementation for later...
        return false;
    }

    public void add(User user) {
        // NEEDS FULL IMPLEMENTATION
        // if user.isValid() returns false or isDup(user) returns true, DO NOT proceed
        firestore.collection("users").add(user.getData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Registration success!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Registration failure!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static boolean isDup(User user) {
        return false;
    }
}
