package ca.uottawa.team3.rentron;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserCreator extends Application {
    Context context;
    public UserCreator(Context activeContext) {
        context = activeContext;
    }
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // returns false if user addition failed, else returns true
    protected boolean add(User user) {
        // if user.isValid() returns false or doesExist(user) returns true, DO NOT proceed
        if (!user.isValid()) {
            Toast.makeText(context, "Registration failure, invalid user details.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (LoginActivity.AuthManager.doesExist(user)) {
            Toast.makeText(context, "Registration failure, user already exists.", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            final boolean[] success = {false}; // hacky way to implement a return value with async Firebase functions
            firestore.collection("users").add(user.getData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    success[0] = true;
                    Toast.makeText(getApplicationContext(), "Registration success!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Registration failure (backend)", Toast.LENGTH_LONG).show();
                }
            });
            Log.d("Success bool:", Boolean.toString(success[0]));
            return success[0];
        }
    }
}
