package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.HashMap;
import java.util.Map;

public class Ticket implements Message {
    private HashMap<String, Object> ticketData;

    public Ticket(String idClient, String idPropertyMgr, String property, String text, int urgency) {
        ticketData = new HashMap<>();
        this.ticketData.put("idClient", idClient);
        this.ticketData.put("idPropertyMgr", idPropertyMgr);
        this.ticketData.put("property", property);
        this.ticketData.put("type", property);
        this.ticketData.put("message", text);
        if (urgency > 5) {
            this.ticketData.put("urgency", new Integer(5));
        } else if (urgency < 1) {
            this.ticketData.put("urgency", new Integer(1));
        } else {
            this.ticketData.put("urgency", urgency);
        }
    }

    @Override
    public Map<String, Object> getData() {
        return ticketData;
    }

    public String getClient() {
        return (String)this.ticketData.get("idClient");
    }
    public String getPropertyMgr() {
        return (String)this.ticketData.get("idPropertyMgr");
    }
    public String getProperty() {
        return (String)this.ticketData.get("property");
    }

    public String getText() {
        return (String) ticketData.get("text");
    }

    @Override
    public boolean isValid() {
        return !(this.getClient().isEmpty() || this.getPropertyMgr().isEmpty() || this.getProperty().isEmpty() || this.getText().isEmpty());
    }
}
