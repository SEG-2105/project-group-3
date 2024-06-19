package ca.uottawa.team3.rentron.Users;

import java.util.Objects;

import javax.crypto.SecretKey;

public class Client extends User { // least privileged type of user

    public Client(String firstName, String lastName, String email, SecretKey password, byte[] salt, String birthYear) {
        super(firstName, lastName, email, password, salt);
        userData.put("birthyear", birthYear);
        userData.put("role", "client");
    }

    public Client(String firstName, String lastName, String email, String birthYear) {
        super(firstName, lastName, email);
        userData.put("birthyear", birthYear);
        userData.put("role", "client");
        userData.put("password", "");
        userData.put("salt", "");
    }

    public Object getBirthYear() {
        return this.userData.get("birthyear");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals("") ||  this.getBirthYear().equals(""));
    }
}
