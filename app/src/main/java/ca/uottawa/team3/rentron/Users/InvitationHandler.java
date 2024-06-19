package ca.uottawa.team3.rentron.Users;

import android.util.Log;

import java.util.Objects;

import ca.uottawa.team3.rentron.Properties.Property;

// TO BE EXPANDED UPON IN THE UPCOMING DELIVERABLES
public class InvitationHandler {
    private PropertyMgr manager;
    private Property property;

    public InvitationHandler(PropertyMgr manager, Property property) {
        this.manager = manager;
        this.property = property;
    }

    public void sendInviteToManager() {
        if (!Objects.isNull(manager)) {
            manager.addInvitation(property.getAddress());
        } else {
            Log.d("InvitationHandler:", "Could not send invite to manager!");
            return;
        }
        // clear selected PropertyMgr's invitation list since, for now, all invitations are automatically accepted
        manager.clearInvitations();
    }

}
