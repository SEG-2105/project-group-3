package ca.uottawa.team3.rentron.Users;

import java.util.Objects;

import javax.crypto.SecretKey;

public class PropertyMgr extends User {
    public PropertyMgr(String firstName, String lastName, String email, SecretKey password, byte[] salt) {
        super(firstName, lastName, email, password, salt);
        userData.put("role", "property-manager");
        userData.put("avgRating", 0.0);
        userData.put("numRatings", 0);
        userData.put("numTicketsHandled", 0);
    }

    // constructors WITHOUT passwords (usually used when generating in-app user lists)
    public PropertyMgr(String firstName, String lastName, String email, double avgRating,
                       int numRatings, int numTicketsHandled) {
        super(firstName, lastName, email);
        userData.put("role", "property-manager");
        userData.put("avgRating", avgRating);
        userData.put("numRatings", numRatings);
        userData.put("numTicketsHandled", numTicketsHandled);
    }

    public PropertyMgr(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        userData.put("role", "property-manager");
        userData.put("avgRating", 0.0);
        userData.put("numRatings", 0);
        userData.put("numTicketsHandled", 0);
    }

    public double getAvgRating() {
        double avgRating = (Double) this.userData.get("avgRating");
        return Math.round(100 * avgRating) / 100.0;
    }
    public int getNumRatings() {
        return (int)userData.get("numRatings");
    }
    public int getNumTicketsHandled() {
        return (int)userData.get("numTicketsHandled");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals(""));
    }

}
