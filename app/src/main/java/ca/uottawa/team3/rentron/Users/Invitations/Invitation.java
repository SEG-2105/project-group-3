package ca.uottawa.team3.rentron.Users.Invitations;

import java.util.HashMap;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Users.*;

public class Invitation {
    private HashMap<String, Object> invitationData;

    public Invitation(Property property, PropertyMgr manager, Landlord landlord, double commission) {
        this.invitationData.put("property", property.getAddress());
        this.invitationData.put("manager", manager.getEmail());
        this.invitationData.put("landlord", landlord.getEmail());
        if ((commission >= 0) && (commission < 100)) { // commission percent has to be above (or eq. to) 0, or below 100
            this.invitationData.put("commission", (Double)commission);
        }
        else {
            this.invitationData.put("commission", new Double(0));
        }
    }

    public HashMap<String, Object> getInvitationData() {
        return this.invitationData;
    }

    public String getLandlordEmail() {
        return (String)this.invitationData.get("landlord");
    }
    public String getManagerEmail() {
        return (String)this.invitationData.get("manager");
    }
    public String getPropertyAddress() {
        return (String)this.invitationData.get("property");
    }
    public Double getCommission() {
        return (Double)this.invitationData.get("commission");
    }
}
