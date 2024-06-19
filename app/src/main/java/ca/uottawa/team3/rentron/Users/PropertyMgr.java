package ca.uottawa.team3.rentron.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;

import ca.uottawa.team3.rentron.Users.Invitations.Invitation;

public class PropertyMgr extends User {
    // WILL BE EXPANDED ON / USED IN LATER DELIVERABLES
    private ArrayList<Invitation> invitations;

    public PropertyMgr(String firstName, String lastName, String email, SecretKey password, byte[] salt) {
        super(firstName, lastName, email, password, salt);
        invitations = new ArrayList<Invitation>();
        userData.put("role", "property-manager");
        userData.put("invitations", "");
    }

    // constructor WITHOUT passwords (usually used when generating in-app user lists)
    public PropertyMgr(String firstName, String lastName, String email) {
        super(firstName, lastName, email);
        invitations = new ArrayList<Invitation>();
        userData.put("role", "property-manager");
        userData.put("invitations", "");
    }

    public boolean isValid() {
        return !(this.getFirstName().equals("") || this.getLastName().equals("") || this.getEmail().equals("") || Objects.isNull(this.getPassword()) || this.getRole().equals(""));
    }

    // get, add, remove, and clear invitations list
    // WILL BE EXPANDED ON / USED IN LATER DELIVERABLES
    public List<Invitation> getInvitations() {
        return this.invitations;
    }

    public void addInvitation(Invitation invite) {
        this.invitations.add(invite);
        this.userData.put("invitations", invitations);
    }

    public boolean removeInvitation(Invitation invite) {
        try {
            this.invitations.remove(invite);
            this.userData.put("invitations", invitations);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clearInvitations() {
        try {
            this.invitations.clear();
            this.userData.put("invitations", "");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
