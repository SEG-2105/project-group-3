package ca.uottawa.team3.rentron;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserCreator extends Application {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    protected void add(User user) {
        // if user.isValid() returns false or doesExist(user) returns true, DO NOT proceed
        if (!user.isValid()) {
            Toast.makeText(getApplicationContext(), "Registration failure, invalid user details.", Toast.LENGTH_LONG).show();
        }
        else if (LoginActivity.AuthManager.doesExist(user)) {
            Toast.makeText(getApplicationContext(), "Registration failure, user already exists.", Toast.LENGTH_LONG).show();
        }
        else {
            firestore.collection("users").add(user.getData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Registration success!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Registration failure (backend)!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
