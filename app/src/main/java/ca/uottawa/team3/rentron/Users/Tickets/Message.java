package ca.uottawa.team3.rentron.Users.Tickets;

import java.util.Map;

public interface Message {
    Map<String, Object> getData();
    boolean isValid();
}
