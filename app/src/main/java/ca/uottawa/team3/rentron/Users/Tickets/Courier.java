package ca.uottawa.team3.rentron.Users.Tickets;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Courier extends Application {
    Context context;
    FirebaseFirestore firestore;

    public Courier(Context context, FirebaseFirestore firestore) {
        this.context = context;
        this.firestore = firestore;
    }

    // send specified request to designated firebase collections (based on type)
    public void send(Request request) {
        if(!request.isValid()) {
            Toast.makeText(context, "Specified request is invalid, make sure all fields are populated.", Toast.LENGTH_SHORT).show();
        }
        else if (doesExist(request)) {
            Toast.makeText(context, "Request already exists.", Toast.LENGTH_SHORT).show();
        }
        else {
            // make sure request is being directed into correct collection
            String type = getCollectionType(request);

            firestore.collection(type).add(request.getRequestData()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

    // Needs actual implementation, commented out code is unfinished
    private boolean doesExist(Request request) {
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

    private String getCollectionType(Request request) {
        if (request instanceof Invitation) {
            return "invitations";
        } else if (request instanceof Ticket) {
            return "tickets";
        } else {
            return "requests";
        }
    }
}
