package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Request implements Message {
    private HashMap<String, Object> requestData;

    public Request(String idClient, String idLandlord, String property) {
        requestData = new HashMap<>();
        this.requestData.put("idClient", idClient);
        this.requestData.put("idLandlord", idLandlord);
        this.requestData.put("property", property);
        this.requestData.put("rejected", false);
    }

    public Request() { // do nothing
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
    public Boolean getRejected() {
        return (Boolean)this.requestData.get("rejected");
    }

    @Override
    public Map<String, Object> getData() {
        return requestData;
    }

    @Override
    public boolean isValid() {
        return !(this.getClient().isEmpty() || this.getLandlord().isEmpty() || this.getProperty().isEmpty());
    }

}
