package ca.uottawa.team3.rentron;

public class Landlord extends User {
    public Landlord(String firstName, String lastName, String email, String password, String address) {
        super(firstName, lastName, email, password);
        userData.put("address", address);
        userData.put("role", "landlord");
    }

    public Object getAddress() {
        return this.userData.get("address");
    }

    public boolean isValid() {
        // needs implementation...
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || this.getPassword().equals("") || this.getRole().equals("") ||  this.getAddress().equals(""));
    }
}
