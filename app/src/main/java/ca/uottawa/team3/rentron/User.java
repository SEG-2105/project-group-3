package ca.uottawa.team3.rentron;

import java.util.HashMap;
import java.util.Map;

public abstract class User { // encapsulates the user's HashMap (Firebase document)
    protected Map<String, Object> userData; // what is passed to Firebase DB

    /* CAUTION:
    /  Current password implementation is UNSAFE, passwords are passed through as Strings.
    /  We should find a safer method to store passwords (through hashing?)
    */
    public User(String firstName, String lastName, String email, String password) {
        userData = new HashMap<>();
        userData.put("firstname", firstName);
        userData.put("lastname", lastName);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("role", "");
    }

    // sanity-checking method to ensure user's HashMap is valid (ie. all fields are populated)
    public abstract boolean isValid();

    // getters/setters
    public Map<String, Object> getData() {
        return userData;
    }
    public Object getEmail() {
        return userData.get("email");
    }

    protected Object getPassword() {
        return userData.get("password");
    }

    public Object getRole() {
        return userData.get("role");
    }

    public Object getFirstName() {
        return userData.get("firstname");
    }
    public Object getLastName() {
        return userData.get("lastname");
    }
}
