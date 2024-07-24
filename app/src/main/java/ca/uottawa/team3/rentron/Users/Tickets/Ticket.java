package ca.uottawa.team3.rentron.Users.Tickets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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

    public Ticket(String idClient, String idPropertyMgr, String property, String type, String message, int urgency, String name, int event, String rating) {
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

        // TICKET STATES:
        // To-Do (1) -> In-Progress (2) -> Closed (Resolved) (3) -> Rated (4)
        // OR: To-Do (1) -> Closed (Rejected) (3) -> Rated (4)
        this.ticketData.put("Status", "To-Do");
        this.ticketData.put("rating", rating);
    }

    public void addMessage(String incomingMessage) {
        String oldMessage = (String)this.ticketData.get("messageCreation");
        String newMessage = oldMessage + "\n\n" + incomingMessage;

        this.ticketData.put("messageCreation", newMessage);
    }

    @Override
    public Map<String, Object> getData() {
        return ticketData;
    }

    public String getClient() { return (String)this.ticketData.get("idClient"); }

    public String getName() { return (String)this.ticketData.get("name"); }

    public int getEvent() { return (int)this.ticketData.get("Event"); }

    // true if successful, false if failed
    public boolean setEvent(int eventIndex) {
        switch (eventIndex) {
            case 1:
                setStatus("To-Do");
                ticketData.put("Event", eventIndex);
                return true;
            case 2:
                setStatus("In-Progress");
                ticketData.put("Event", eventIndex);
                return true;
            case 3:
                setStatus("Closed");
                ticketData.put("Event", eventIndex);
                return true;
            case 4:
                setStatus("Rated");
                ticketData.put("Event", eventIndex);
                return true;
            default:
                return false;
        }
    }

    private void setStatus(String status) {
        this.ticketData.put("Status", status);
    }

    public String getStatus() { return (String) ticketData.get("Status"); }

    public String getRating() { return (String)this.ticketData.get("rating"); }

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
