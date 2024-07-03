package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.HashMap;

public class Ticket extends Request {
    private HashMap<String, Object> ticketData;

    public Ticket(String idClient, String idLandlord, String property, String text) {
        super(idClient, idLandlord, property);
        ticketData = super.getRequestData();
        ticketData.put("text", text);
    }

    public String getText() {
        return (String) ticketData.get("text");
    }

    @Override
    public boolean isValid() {
        return !(this.getClient().isEmpty() || this.getLandlord().isEmpty() || this.getProperty().isEmpty() || this.getText().isEmpty());
    }
}
