package ca.uottawa.team3.rentron;

import java.util.HashMap;
import java.util.Map;

public abstract class User { // encapsulates the user's HashMap (Firebase document)
    Map<String, Object> userData; // what is passed to Firebase DB

    /* CAUTION:
    /  Current password implementation is UNSAFE, passwords are passed through as Strings. (this applies to all User subclasses as well)
    /  We need to find a safer method to store passwords (some type of hash?)
    */
    public User(String firstName, String lastName, String email, String password) {
        userData = new HashMap<>();
        userData.put("firstname", firstName);
        userData.put("lastname", lastName);
        userData.put("email", email);
        userData.put("password", password);
    }

    // potential sanity-checking method to ensure user's HashMap is valid (ie. all fields are populated)
    // implement later...
    public abstract boolean isValid();


    // getters/setters
    public Map<String, Object> getData() {
        return userData;
    }
    public Object getEmail() {
        return userData.get("email");
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
