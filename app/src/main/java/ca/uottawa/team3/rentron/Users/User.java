package ca.uottawa.team3.rentron.Users;

import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

public abstract class User { // encapsulates the user's HashMap (Firebase document)
    protected Map<String, Object> userData; // what is passed to Firebase DB

    /* CAUTION:
    /  Current password implementation is UNSAFE, passwords are passed through as Strings.
    /  We should find a safer method to store passwords (through hashing?)
    */
    public User(String firstName, String lastName, String email, SecretKey password, byte[] salt) {
        String encodedPassword = "";
        String encodedSalt = "";
        if (password != null) {
            encodedPassword = Base64.encodeToString(password.getEncoded(), Base64.DEFAULT);
            encodedSalt = Base64.encodeToString(salt, Base64.DEFAULT);
        }
        userData = new HashMap<>();
        userData.put("firstname", firstName);
        userData.put("lastname", lastName);
        userData.put("email", email);
        userData.put("password", encodedPassword);
        userData.put("salt", encodedSalt);
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
