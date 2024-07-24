package ca.uottawa.team3.rentron.Users.Messaging;

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
