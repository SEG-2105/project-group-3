package ca.uottawa.team3.rentron.Users.Tickets;

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

public class Courier extends Application {
    Context context;
    FirebaseFirestore firestore;

    public Courier(Context context, FirebaseFirestore firestore) {
        this.context = context;
        this.firestore = firestore;
    }

    // send specified Request to designated firebase collections (based on type)
    public void sendRequest(Message message) {
        if(!message.isValid()) {
            Toast.makeText(context, "Specified request is invalid, make sure all fields are populated.", Toast.LENGTH_SHORT).show();
        }
        else if (doesExist(message)) {
            Toast.makeText(context, "Request already exists.", Toast.LENGTH_SHORT).show();
        }
        else {
            // make sure request is being directed into correct collection
            String type = "requests"; // SET TO CONSTANT AFTER CHANGED TO sendRequest(request)

            firestore.collection(type).add(message.getData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(context, "TicketHandler: Ticket successfully sent!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "TicketHandler: Ticket failed to send", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Needs actual implementation (code from WelcomeActivity should be moved here)
    public void acceptRequest(Request request) {
        // first, get client's name
        String clientEmail = request.getClient();
        String landlordEmail = request.getLandlord();
        CollectionReference dbClient = firestore.collection("requests");
        //...

        CollectionReference db = firestore.collection("requests");
        Task<QuerySnapshot> query; //...
        //... Unfinished.
    }

    // Needs actual implementation, commented out code is unfinished
    private boolean doesExist(Message message) {
//        String type = getCollectionType(request);
//        CollectionReference db = firestore.collection(type);
//        Task<QuerySnapshot> query;
//        switch (type) {
//            case "invitations":
//                query = db.whereEqualTo("idClient", request.getClient()).whereEqualTo("idLandlord", request.getLandlord())
//                        .whereEqualTo("property", request.getProperty()).whereEqualTo("commission", (Invitation)request.getCommission()).get();
//                break;
//            case "tickets":
//                query = db.whereEqualTo("idClient", request.getClient()).whereEqualTo("idLandlord", request.getLandlord())
//                        .whereEqualTo("property", request.getProperty()).get();
//                break;
//            case "requests":
//                query = db.whereEqualTo("idClient", request.getClient()).whereEqualTo("idLandlord", request.getLandlord())
//                        .whereEqualTo("property", request.getProperty()).get();
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + type);
//        }
//        while(!query.isComplete()); // hacky way to wrangle async. Firebase methods, should be changed
//        if (query.isSuccessful()) {
//            if (!query.getResult().isEmpty()) {
//                return true;
//            }
//        }
        return false;
    }

    private String getCollectionType(Message message) {
        if (message instanceof Invitation) {
            return "invitations";
        } else if (message instanceof Ticket) {
            return "tickets";
        } else {
            return "requests";
        }
    }
}
