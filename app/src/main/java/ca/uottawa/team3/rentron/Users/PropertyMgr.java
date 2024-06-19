package ca.uottawa.team3.rentron.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

import ca.uottawa.team3.rentron.Property;

public class PropertyMgr extends User {
    // WILL BE EXPANDED ON / USED IN LATER DELIVERABLES
    private ArrayList<String> invitations;

    public PropertyMgr(String firstName, String lastName, String email, SecretKey password, byte[] salt) {
        super(firstName, lastName, email, password, salt);
        userData.put("role", "property-manager");
        userData.put("invitations", "");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals(""));
    }

    // constructor WITHOUT passwords (used when generating in-app user lists)
    public PropertyMgr(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        userData.put("role", "property-manager");
        userData.put("invitations", "");
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

    public void clearInvitations() {
        this.invitations.clear();
    }

}
