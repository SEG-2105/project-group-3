package ca.uottawa.team3.rentron;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import ca.uottawa.team3.rentron.Properties.Property;

public class EditPropertyActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    Property propertyToEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_property);

        String propertyAddress = getIntent().getStringExtra("property");
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("properties")
                .whereEqualTo("address", propertyAddress)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Property db_property = new Property((String)document.get("address"), (String)document.get("type"), (String)document.get("floor"),
                                        (String)document.get("numRoom"), (String)document.get("numBathroom"), (String)document.get("numFloor"), (String)document.get("area"),
                                        (String)document.get("laundry"), (String)document.get("numParkingSpot"), (String)document.get("rent"), (String)document.get("utilities"),
                                        (String)document.get("landlord"),(String)document.get("manager"),(String)document.get("client"));
                            }
                        } else {
                            Log.d("EditPropertyActivity:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}