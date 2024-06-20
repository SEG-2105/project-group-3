package ca.uottawa.team3.rentron.Properties;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PropertyCreator extends Application {
    Context context;
    FirebaseFirestore firestore;

    public PropertyCreator(Context activeContext, FirebaseFirestore firestore) {
        this.context = activeContext;
        this.firestore = firestore;
    }

    // returns false if user addition failed, else returns true
    public boolean add(Property property) {
        // if user.isValid() returns false or doesExist(user) returns true, DO NOT proceed
        if (!property.isValid()) {
            Toast.makeText(context, "Registration failure, invalid property details.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (doesExist((String)property.getAddress())) {
            Toast.makeText(context, "Property already exists with the same address.", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            firestore.collection("properties").add(property.getPropertyData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(context, "Property registration success! (collection)", Toast.LENGTH_LONG).show();
                    documentReference.getParent();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Property registration failure (collection)", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
    }
    public boolean doesExist(String address) { // no two properties can have the same address
        CollectionReference db = firestore.collection("properties");
        Task<QuerySnapshot> query = db.whereEqualTo("properties", address).get();
        while(!query.isComplete()); // hacky way to wrangle async. Firebase methods, should be changed
        if (query.isSuccessful()) {
            if (!query.getResult().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
