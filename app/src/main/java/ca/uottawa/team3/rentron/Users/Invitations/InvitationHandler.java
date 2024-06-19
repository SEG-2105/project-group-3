package ca.uottawa.team3.rentron.Users.Invitations;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ca.uottawa.team3.rentron.Properties.Property;

import ca.uottawa.team3.rentron.Users.PropertyMgr;

// TO BE EXPANDED UPON IN THE UPCOMING DELIVERABLES
public class InvitationHandler extends Application {
    private PropertyMgr manager;
    private Property property;
    private String landlord;
    private double commission;
    private Invitation invite;

    Context context;
    FirebaseFirestore firestore;

    public InvitationHandler(Property property, PropertyMgr manager, String landlordEmail, double commission) {
        this.manager = manager;
        this.property = property;
        this.landlord = landlord;
        this.commission = commission;
        this.invite = new Invitation(property, (String)manager.getEmail(), landlord, commission);
    }

    public void sendInviteToManager() {
        if (!Objects.isNull(manager)) {

            manager.addInvitation(invite);
        } else {
            Log.d("InvitationHandler:", "Could not send invite to manager!");
            return;
        }
        // clear selected PropertyMgr's invitation list since, for now, all invitations are automatically accepted
        manager.clearInvitations();
    }

}
