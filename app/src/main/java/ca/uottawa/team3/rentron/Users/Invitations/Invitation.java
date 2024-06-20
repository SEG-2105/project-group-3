package ca.uottawa.team3.rentron.Users.Invitations;

import java.util.HashMap;

import ca.uottawa.team3.rentron.Properties.Property;
import ca.uottawa.team3.rentron.Users.*;

public class Invitation {
    private HashMap<String, Object> invitationData;

    public Invitation(String propertyId, String manager, String landlord, double commission) {
        this.invitationData.put("property", propertyId);
        this.invitationData.put("manager", manager);
        this.invitationData.put("landlord", landlord);
        if ((commission >= 0) && (commission < 100)) { // commission percent has to be above (or eq. to) 0, or below 100
            this.invitationData.put("commission", (Double)commission);
        }
        else {
            this.invitationData.put("commission", new Double(0));
        }
        invitationData.put("accepted", (Boolean)false);
        invitationData.put("declined", (Boolean)false);
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

    public boolean isAccepted() {
        return Boolean.parseBoolean(this.invitationData.get("accepted").toString());
    }
    public boolean isDeclined() {
        return Boolean.parseBoolean(this.invitationData.get("declined").toString());
    }

    public void accept() {
        this.invitationData.put("accepted", (Boolean)true);
        this.invitationData.put("declined", (Boolean)false);
    }

    public void decline() {
        this.invitationData.put("accepted", (Boolean)false);
        this.invitationData.put("declined", (Boolean)true);
    }
}
