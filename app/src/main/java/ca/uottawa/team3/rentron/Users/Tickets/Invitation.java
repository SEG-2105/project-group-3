package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.HashMap;
import java.util.Map;

public class Invitation implements Message {
    private HashMap<String, Object> invitationData;

    public Invitation(String idLandlord, String idPropertyMgr, String property, double commission, boolean accepted) {
        invitationData = new HashMap<>();
        this.invitationData.put("idLandlord", idLandlord);
        this.invitationData.put("idPropertyMgr", idPropertyMgr);
        this.invitationData.put("property", property);

        if ((commission >= 0) && (commission < 100)) { // commission percent has to be above (or eq. to) 0, or below 100
            this.invitationData.put("commission", (Double)commission);
        }
        else {
            this.invitationData.put("commission", new Double(0));
        }
        invitationData.put("accepted", accepted);
    }

    @Override
    public Map<String, Object> getData() {
        return invitationData;
    }

    public String getPropertyMgr() {
        return (String)this.invitationData.get("idPropertyMgr");
    }
    public String getLandlord() {
        return (String)this.invitationData.get("idLandlord");
    }
    public String getProperty() {
        return (String)this.invitationData.get("property");
    }


    public double getCommission() { return (double) invitationData.get("commission"); }
    public String getAccepted() { return (String) invitationData.get("accepted"); }

    public void setCommission(double commission) {
        this.invitationData.put("commission", commission);
    }

    public void setAccepted(boolean accepted) {
        this.invitationData.put("accepted", accepted);
    }

    @Override
    public Type getType() {
        return Type.INVITATION;
    }

    @Override
    public boolean isValid() {
        return !(this.getPropertyMgr().isEmpty() || this.getLandlord().isEmpty() || this.getProperty().isEmpty() || ((this.getCommission() < 0.0) || (this.getCommission() >= 100.0)));
    }
}