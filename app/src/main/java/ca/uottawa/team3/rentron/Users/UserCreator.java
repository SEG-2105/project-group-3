package ca.uottawa.team3.rentron.Users;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserCreator extends Application {
    Context context;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public UserCreator(Context activeContext) {
        this.context = activeContext;
    }

    // returns false if user addition failed, else returns true
    public boolean add(User user) {
        // if user.isValid() returns false or doesExist(user) returns true, DO NOT proceed
        if (!user.isValid()) {
            Toast.makeText(context, "Registration failure, invalid user details.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (this.doesExist((String)user.getEmail())) {
            Toast.makeText(context, "Registration failure, user already exists.", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            firestore.collection("users").add(user.getData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(context, "Registration success! (collection)", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Registration failure (collection)", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
    }

    // TODO: STILL NEEDS IMPLEMENTATION
    public boolean doesExist(String email) { // no two users can have the same email
//        CollectionReference db = firestore.collection("users");
//        Task<QuerySnapshot> query = db.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                } else {
//                    Log.d("doesExist():", "Error getting documents: ", task.getException());
//                }
//            }
//        });
//        return query;
        return false;
    }
}
