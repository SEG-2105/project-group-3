package ca.uottawa.team3.rentron.Users;

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

import javax.crypto.SecretKey;

public class UserCreator extends Application {
    Context context;
    FirebaseFirestore firestore;

    public UserCreator(Context activeContext, FirebaseFirestore firestore) {
        this.context = activeContext;
        this.firestore = firestore;
    }

    // returns false if user addition failed, else returns true
    public boolean add(User user, String pass) {
        // if user.isValid() returns false or doesExist(user) returns true, DO NOT proceed
        if (!user.isValid()) {
            Toast.makeText(context, "Registration failure, invalid user details.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (doesExist((String)user.getEmail())) {
            Toast.makeText(context, "User already exists with the same email.", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            // password hashing logic
            Hashing hasher = new Hashing(context);
            byte[] salt = hasher.generateSalt();
            SecretKey password = hasher.hashPassword(pass.toCharArray(), salt);

            user.setPassword(password, salt); // add password hash/salt to user document before uploading

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
        CollectionReference db = firestore.collection("users");
        Task<QuerySnapshot> query = db.whereEqualTo("email", email).get();
        while(!query.isComplete()); // hacky way to wrangle async. Firebase methods, should be changed
        if (query.isSuccessful()) {
            if (!query.getResult().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
