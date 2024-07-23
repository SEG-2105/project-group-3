package ca.uottawa.team3.rentron;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ca.uottawa.team3.rentron.Users.PropertyMgr;

public class SelectPropertyMgrActivity extends AppCompatActivity {

    boolean isOccupied;
    ListView propertyMgrListView;
    Button unassign;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_property_mgr);
        Toolbar topBar = findViewById(R.id.topBar);

        db = FirebaseFirestore.getInstance();
        propertyMgrListView = findViewById(R.id.listViewPropertyMgrs);
        unassign = findViewById(R.id.btnUnassignManager);

        isOccupied = getIntent().getBooleanExtra("isOccupied", false);

        if (isOccupied) {
            unassign.setVisibility(View.GONE);
        } else {
            unassign.setVisibility(View.VISIBLE);
        }

        ArrayList<PropertyMgr> propertyMgrList = new ArrayList<PropertyMgr>();

        db.collection("users").whereEqualTo("role", "property-manager")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // the following code is to prevent ClassCastExceptions. consider changing these fields to strings?
                                Number avgRatingNum = (Number) document.get("avgRating");
                                Double avgRating = (avgRatingNum != null) ? avgRatingNum.doubleValue():null;
                                Number numRatingsNum = (Number) document.get("numRatings");
                                Integer numRatings = (numRatingsNum != null) ? numRatingsNum.intValue() : null;
                                Number numTicketsHandledNum = (Number) document.get("numTicketsHandled");
                                Integer numTicketsHandled = (numTicketsHandledNum != null) ? numTicketsHandledNum.intValue() : null;

                                PropertyMgr propertyMgr = new PropertyMgr((String) document.get("firstname"), (String) document.get("lastname"),
                                        (String) document.get("email"), avgRating, numRatings, numTicketsHandled);
                                propertyMgrList.add(propertyMgr);
                            }
                            PropertyMgrListAdapter mgrsAdapter = new PropertyMgrListAdapter(SelectPropertyMgrActivity.this, propertyMgrList);

                            propertyMgrListView.setAdapter(mgrsAdapter);
                        } else {
                            Log.d("SelectPropertyMgrActivity:", "Error getting documents: ", task.getException());
                        }
                    }
                });

        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivityForResult (intent,0);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}