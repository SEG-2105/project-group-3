package ca.uottawa.team3.rentron.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

public class PropertyMgr extends User {
    // WILL BE EXPANDED ON / USED IN LATER DELIVERABLES
    private ArrayList<String> invitations;

    public PropertyMgr(String firstName, String lastName, String email, SecretKey password, byte[] salt) {
        super(firstName, lastName, email, password, salt);
        invitations = new ArrayList<String>();
        userData.put("role", "property-manager");
        userData.put("invitations", "");
    }

    // constructor WITHOUT passwords (used when generating in-app user lists)
    public PropertyMgr(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        invitations = new ArrayList<String>();
        userData.put("role", "property-manager");
        userData.put("invitations", "");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals(""));
    }

    // get, add, remove, and clear invitations list
    // WILL BE EXPANDED ON / USED IN LATER DELIVERABLES
    public List<String> getInvitations() {
        return this.invitations;
    }

    public void addInvitation(String propertyId) {
        this.invitations.add(propertyId);
    }

    public boolean removeInvitation(String propertyId) {
        try {
            this.invitations.remove(propertyId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clearInvitations() {
        try {
            this.invitations.clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
