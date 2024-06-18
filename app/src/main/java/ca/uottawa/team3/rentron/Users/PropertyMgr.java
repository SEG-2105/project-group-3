package ca.uottawa.team3.rentron.Users;

import java.util.HashMap;
import java.util.Objects;

import javax.crypto.SecretKey;

public class PropertyMgr extends User {
    public PropertyMgr(String firstName, String lastName, String email, SecretKey password, byte[] salt) {
        super(firstName, lastName, email, password, salt);
        userData.put("role", "property-manager");
    }

    // constructor WITHOUT passwords (used when generating in-app user lists)
    public PropertyMgr(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        userData.put("role", "property-manager");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals(""));
    }
}
