package ca.uottawa.team3.rentron;

public class Landlord extends User {
    public Landlord(String firstName, String lastName, String email, String password, String address) {
        super(firstName, lastName, email, password);
        userData.put("address", address);
        userData.put("role", "landlord");
    }

    public boolean isValid() {
        // needs implementation...
        return false;
    }
}
