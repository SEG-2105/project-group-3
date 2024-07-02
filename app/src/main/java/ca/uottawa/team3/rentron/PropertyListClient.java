package ca.uottawa.team3.rentron;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.uottawa.team3.rentron.Properties.Property;

public class PropertyListClient extends ArrayAdapter<Property> {

    private Activity context;
    List<Property> properties;

    public PropertyListClient(Activity context, List<Property> properties) {
        super(context, R.layout.layout_property_item, properties);
        this.context = context;
        this.properties = properties;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_property_item_client, null, true);

        TextView address = listViewItem.findViewById(R.id.textViewAddress);
        TextView type = listViewItem.findViewById(R.id.textViewClient);
        TextView rent = listViewItem.findViewById(R.id.textViewRent);

        Property property = properties.get(position);
        address.setText(property.getAddress());
        type.setText(property.getType());
        String rent1 = "Rent:$"+property.getRent()+"/month";
        rent.setText(rent1);

        return listViewItem;
    }


}
