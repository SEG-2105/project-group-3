package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.HashMap;
import java.util.Map;

public class Ticket implements Message {
    private HashMap<String, Object> ticketData;

    public Ticket(String idClient, String idPropertyMgr, String property, String type, String message, int urgency, String name) {
        ticketData = new HashMap<>();
        this.ticketData.put("idClient", idClient);
        this.ticketData.put("idPropertyMgr", idPropertyMgr);
        this.ticketData.put("property", property);

        // Maintenance, Security, Damage, Infestation
        this.ticketData.put("type", type);

        this.ticketData.put("name", name);

        this.ticketData.put("messageCreation", message);
        if (urgency > 5) {
            this.ticketData.put("urgency", new Integer(5));
        } else if (urgency < 1) {
            this.ticketData.put("urgency", new Integer(1));
        } else {
            this.ticketData.put("urgency", urgency);
        }

        this.ticketData.put("Event", 1);

        // To-Do, In-progress, Rejected, Resolved
        this.ticketData.put("Status", "To-Do");
        this.ticketData.put("rating", "");
    }

    public Ticket(String idClient, String idPropertyMgr, String property, String type, String message, int urgency, String name, int event) {
        ticketData = new HashMap<>();
        this.ticketData.put("idClient", idClient);
        this.ticketData.put("idPropertyMgr", idPropertyMgr);
        this.ticketData.put("property", property);

        // Maintenance, Security, Damage, Infestation
        this.ticketData.put("type", type);

        this.ticketData.put("name", name);

        this.ticketData.put("messageCreation", message);
        if (urgency > 5) {
            this.ticketData.put("urgency", new Integer(5));
        } else if (urgency < 1) {
            this.ticketData.put("urgency", new Integer(1));
        } else {
            this.ticketData.put("urgency", urgency);
        }

        this.ticketData.put("Event", event);

        // To-Do, In-progress, Rejected, Resolved
        this.ticketData.put("Status", "To-Do");
        this.ticketData.put("rating", "");
    }


    public void AddMessage(String incomingMessage) {
        String oldMessage = (String)this.ticketData.get("message");
        String newMessage = oldMessage + "\n\n" + incomingMessage;

        this.ticketData.put("message", newMessage);
    }

    @Override
    public Map<String, Object> getData() {
        return ticketData;
    }

    public String getClient() { return (String)this.ticketData.get("idClient"); }

    public String getName() { return (String)this.ticketData.get("name"); }

    public int getEvent() { return (int)this.ticketData.get("Event"); }

    public String getPropertyMgr() { return (String)this.ticketData.get("idPropertyMgr");}

    public String getProperty() { return (String)this.ticketData.get("property"); }

    public String getText() { return (String) ticketData.get("messageCreation"); }

    public String getTicketType() { return (String) ticketData.get("type"); }

    @Override
    public Type getType() { return Type.TICKET; }

    @Override
    public boolean isValid() {
        return !(this.getClient().isEmpty() || this.getPropertyMgr().isEmpty() || this.getProperty().isEmpty() || this.getText().isEmpty());
    }
}
