package ca.uottawa.team3.rentron;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        else if (this.doesExist(user)) {
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

    public boolean doesExist(User user) { // no two users can have the same email
        CollectionReference db = firestore.collection("users");
        Task<QuerySnapshot> query = db.whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("doesExist():", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("doesExist():", "Error getting documents: ", task.getException());
                }
            }
        });
        return !query.getResult().isEmpty();
    }
}
