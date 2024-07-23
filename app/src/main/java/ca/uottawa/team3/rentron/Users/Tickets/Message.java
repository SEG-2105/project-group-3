package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.Map;

public interface Message {
    enum Type {
        INVITATION,
        REQUEST,
        TICKET
    }
    Map<String, Object> getData();
    Type getType();
    boolean isValid();
}
