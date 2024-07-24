package ca.uottawa.team3.rentron;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uottawa.team3.rentron.Users.Messaging.Invitation;

public class InvitationListAdapter extends ArrayAdapter<Invitation> {

    private Activity context;
    List<Invitation> invitations;

    public InvitationListAdapter(Activity context, List<Invitation> invitations) {
        super(context, R.layout.layout_property_item, invitations);
        this.context = context;
        this.invitations = invitations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_invitation_item, null, true);

        TextView address = listViewItem.findViewById(R.id.textViewInviteAddress);
        TextView landlord = listViewItem.findViewById(R.id.textViewInviteLandlord);
        TextView commission = listViewItem.findViewById(R.id.textViewInviteCommission);

        Invitation invite = invitations.get(position);
        address.setText(invite.getProperty());
        landlord.setText(invite.getLandlord());
        commission.setText("Commission %: " + Double.toString(invite.getCommission()));

        return listViewItem;
    }
}
