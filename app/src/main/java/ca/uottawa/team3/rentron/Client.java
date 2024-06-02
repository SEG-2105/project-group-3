package ca.uottawa.team3.rentron;

public class Client extends User { // least privileged type of user
    public Client(String firstName, String lastName, String email, String password, String birthYear) {
        super(firstName, lastName, email, password);
        userData.put("birthyear", birthYear);
        userData.put("role", "client");
    }

    public Object getBirthYear() {
        return this.userData.get("birthyear");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || this.getData().get("password").equals("") || this.getRole().equals("") ||  this.getBirthYear().equals(""));
    }
}
