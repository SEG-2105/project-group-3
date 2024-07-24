package ca.uottawa.team3.rentron;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uottawa.team3.rentron.Users.Messaging.Request;

public class RequestListAdapter extends ArrayAdapter<Request> {

    private Activity context;
    List<Request> requests;

    public RequestListAdapter(Activity context, List<Request> requests) {
        super(context, R.layout.layout_property_item, requests);
        this.context = context;
        this.requests = requests;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_request_item, null, true);

        TextView address = listViewItem.findViewById(R.id.textViewAddress);
        TextView client = listViewItem.findViewById(R.id.textViewClient);

        Request request = requests.get(position);
        address.setText(request.getProperty());
        client.setText(request.getClient());

        return listViewItem;
    }
}
