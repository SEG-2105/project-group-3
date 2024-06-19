package ca.uottawa.team3.rentron.Users.Invitations;

import android.util.Log;

import java.util.Objects;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Users.Landlord;
import ca.uottawa.team3.rentron.Users.PropertyMgr;

// TO BE EXPANDED UPON IN THE UPCOMING DELIVERABLES
public class InvitationHandler {
    private PropertyMgr manager;
    private Property property;
    private Landlord landlord;
    private double commission;
    private Invitation invite;

    public InvitationHandler(Property property, PropertyMgr manager, Landlord landlord, double commission) {
        this.manager = manager;
        this.property = property;
        this.landlord = landlord;
        this.commission = commission;
        this.invite = new Invitation(property, manager, landlord, commission);
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
