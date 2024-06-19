package ca.uottawa.team3.rentron.Users;

import java.util.Objects;

import javax.crypto.SecretKey;

public class Landlord extends User {
    public Landlord(String firstName, String lastName, String email, SecretKey password, byte[] salt, String address) {
        super(firstName, lastName, email, password, salt);
        userData.put("address", address);
        userData.put("role", "landlord");
    }

    public Landlord(String firstName, String lastName, String email, String address) {
        super(firstName, lastName, email);
        userData.put("address", address);
        userData.put("role", "landlord");
        userData.put("password", "");
        userData.put("salt", "");
    }

    public Object getAddress() {
        return this.userData.get("address");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals("") ||  this.getAddress().equals(""));
    }
}
