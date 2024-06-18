package ca.uottawa.team3.rentron;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PropertyListLandlord extends ArrayAdapter<Property> {

    private Activity context;
    List<Property> properties;

    public PropertyListLandlord(Activity context, List<Property> properties) {
        super(context, R.layout.layout_property_item, properties);
        this.context = context;
        this.properties = properties;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_property_item, null, true);

        TextView address = listViewItem.findViewById(R.id.textViewAddress);
        TextView type = listViewItem.findViewById(R.id.textViewType);
        TextView floor = listViewItem.findViewById(R.id.textViewFloor);
        TextView numRooms = listViewItem.findViewById(R.id.textViewNumberOfRooms);
        TextView numBathrooms = listViewItem.findViewById(R.id.textViewNumberOfBathrooms);
        TextView numFloors = listViewItem.findViewById(R.id.textViewNumberOfFloors);
        TextView area = listViewItem.findViewById(R.id.textViewTotalArea);
        TextView laundry = listViewItem.findViewById(R.id.textViewLaundry);
        TextView parking = listViewItem.findViewById(R.id.textViewParking);
        TextView rent = listViewItem.findViewById(R.id.textViewRent);
        TextView utilities = listViewItem.findViewById(R.id.textViewUtilities);
        TextView manager = listViewItem.findViewById(R.id.textViewManager);
        TextView client = listViewItem.findViewById(R.id.textViewClient);

        Property property = properties.get(position);
        address.setText(property.getAddress());
        type.setText(property.getType());
        String floorString = "Floor: "+property.getFloor();
        floor.setText(floorString);
        numRooms.setText(property.getNumRoom());
        numBathrooms.setText(property.getNumBathroom());
        numFloors.setText(property.getNumFloor());
        area.setText(property.getArea());
        laundry.setText((property.getLaundry()) ? "Laundry: Yes":"Laundry: No");
        parking.setText(property.getNumParkingSpot());
        rent.setText(property.getRent());
        utilities.setText(property.getUtilities());
        if (property.getManager().isEmpty()){
            manager.setText("No Manager");
        } else {
            manager.setText(property.getManager());
        }

        if (property.getClient().isEmpty()){
            client.setText("No Client");
        } else {
            client.setText(property.getManager());
        }
        Toast.makeText(context, "Instantiated list!!", Toast.LENGTH_SHORT).show();
        return listViewItem;
    }


}
