package ca.uottawa.team3.rentron;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.uottawa.team3.rentron.Properties.Property;

public class PropertyList  extends ArrayAdapter<Property> {

    private Activity context;
    List<Property> properties;

    public PropertyList(Activity context, List<Property> properties) {
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

        Property property = properties.get(position);
        address.setText(property.getAddress());
        type.setText(property.getType());
        String floorString = "Floor: "+property.getFloor() + " Unit: "+property.getUnit();
        floor.setText(floorString);
        String numRoom = "Number of Rooms:"+property.getNumRoom();
        numRooms.setText(numRoom);
        String numBathroom = "Number of Bathrooms:"+property.getNumBathroom();
        numBathrooms.setText(numBathroom);
        String numFloor = "Number of Floors:"+property.getNumFloor();
        numFloors.setText(numFloor);
        String area1 = "Area:"+property.getArea()+" s.q. ft";
        area.setText(area1);
        String laundry1 = "Laundry:"+property.getLaundry();
        laundry.setText(laundry1);
        String numParkingSpot = "Number of Parking spots:"+property.getNumParkingSpot();
        parking.setText(numParkingSpot);
        String rent1 = "Rent:$"+property.getRent()+"/month";
        rent.setText(rent1);

        String utilities1 = "Utilities:";
        if (property.getHeating()) {
            utilities1 += ("Heating, ");
        }
        if (property.getHydro()) {
            utilities1 += ("Hydro, ");
        }
        if (property.getWater()) {
            utilities1 += ("Water, ");
        }
        if (property.getHeating() || property.getHydro() || property.getWater()) {
            utilities.setText(utilities1.substring(0, utilities1.length() - 2));
        } else {
            utilities.setText("No Utilities");
        }

        return listViewItem;
    }


}
