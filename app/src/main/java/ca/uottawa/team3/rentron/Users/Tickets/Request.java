package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.HashMap;

public class Request {
    private HashMap<String, Object> requestData;

    public Request(String idClient, String idLandlord, String property) {
        requestData = new HashMap<>();
        this.requestData.put("idClient", idClient);
        this.requestData.put("idLandlord", idLandlord);
        this.requestData.put("property", property);
        this.requestData.put("rejected", false);
    }
    public String getClient() {
        return (String)this.requestData.get("idClient");
    }
    public String getLandlord() {
        return (String)this.requestData.get("idLandlord");
    }
    public String getProperty() {
        return (String)this.requestData.get("property");
    }
    public HashMap<String, Object> getRequestData() {
        return (HashMap<String, Object>) requestData;
    }

    public boolean isValid() {
        return !(this.getClient().isEmpty() || this.getLandlord().isEmpty() || this.getProperty().isEmpty());
    }
}
