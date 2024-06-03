package ca.uottawa.team3.rentron;

public class PropertyMgr extends User {
    public PropertyMgr(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        userData.put("role", "property-manager");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || this.getPassword().equals("") || this.getRole().equals(""));
    }
}
