package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.HashMap;

public class Invitation extends Request {
    private HashMap<String, Object> invitationData;

    public Invitation(String idClient, String idLandlord, String property, double commission, boolean accepted) {
        super(idClient, idLandlord, property);
        invitationData = super.getRequestData();
        if ((commission >= 0) && (commission < 100)) { // commission percent has to be above (or eq. to) 0, or below 100
            this.invitationData.put("commission", (Double)commission);
        }
        else {
            this.invitationData.put("commission", new Double(0));
        }
        invitationData.put("accepted", accepted);
    }

    public String getCommission() { return (String) invitationData.get("commission"); }
    public String getAccepted() { return (String) invitationData.get("accepted"); }

    public void setCommission(double commission) {
        this.invitationData.put("commission", commission);
    }

    public void setAccepted(boolean accepted) {
        this.invitationData.put("accepted", accepted);
    }
}