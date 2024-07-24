package ca.uottawa.team3.rentron.Users.Messaging;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Courier extends Application {
    Context context;
    FirebaseFirestore firestore;

    public Courier(Context context, FirebaseFirestore firestore) {
        this.context = context;
        this.firestore = firestore;
    }

    private String getCollectionType(Message.Type type) {
        switch (type) {
            case INVITATION:
                return "invitations";
            case REQUEST:
                return "requests";
            case TICKET:
                return "tickets";
            default:
                throw new IllegalStateException("Unexpected type: " + type);
        }
    }

    // send specified Message to designated firebase collections (based on type)
    public void sendMessage(Message message) {
        if(!message.isValid()) {
            Toast.makeText(context, "Specified message is invalid, make sure all fields are populated.", Toast.LENGTH_SHORT).show();
        }
        else if (doesExist(message)) {
            Toast.makeText(context, "Message already exists.", Toast.LENGTH_SHORT).show();
        }
        else {
            // make sure message is directed into correct collection
            String msgCollection = getCollectionType(message.getType());

            firestore.collection(getCollectionType(message.getType())).add(message.getData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(context, "Courier: Message (" + msgCollection + ") successfully sent!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Courier: Message failed to send", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Untested
    private boolean doesExist(Message message) {
        String msgCollection = getCollectionType(message.getType());
        Task<QuerySnapshot> query;
        switch (msgCollection) {
            case "invitations":
                query = firestore.collection(msgCollection).whereEqualTo("property", message.getData().get("property"))
                        .whereEqualTo("idLandlord", message.getData().get("idLandlord"))
                        .whereEqualTo("idPropertyMgr", message.getData().get("idPropertyMgr")).get();
                break;
            case "requests":
                query = firestore.collection(msgCollection).whereEqualTo("property", message.getData().get("property"))
                        .whereEqualTo("idClient", message.getData().get("idClient"))
                        .whereEqualTo("idLandlord", message.getData().get("idLandlord"))
                        .whereEqualTo("rejected", message.getData().get("rejected")).get();
                break;
            case "tickets":
                query = firestore.collection(msgCollection).whereEqualTo("property", message.getData().get("property"))
                        .whereEqualTo("idClient", message.getData().get("idClient"))
                        .whereEqualTo("idPropertyMgr", message.getData().get("idPropertyMgr"))
                        .whereEqualTo("message", message.getData().get("message")).get();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + msgCollection);
        }
        while(!query.isComplete()); // hacky way to wrangle async. Firebase methods, should be changed
        if (query.isSuccessful()) {
            if (!query.getResult().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
