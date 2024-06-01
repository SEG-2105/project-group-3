package ca.uottawa.team3.rentron;

public class Client extends User { // least privileged type of user
    public Client(String firstName, String lastName, String email, String password, String birthYear) {
        super(firstName, lastName, email, password);
        userData.put("birthyear", birthYear);
        userData.put("role", "client");
    }
    public boolean isValid() {
        // needs implementation...
        return false;
    }
}
