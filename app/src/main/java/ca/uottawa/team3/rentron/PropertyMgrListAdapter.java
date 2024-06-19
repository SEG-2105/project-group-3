package ca.uottawa.team3.rentron;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.uottawa.team3.rentron.Users.*;

public class PropertyMgrListAdapter extends ArrayAdapter<PropertyMgr>  {
    private Activity context;
    List<PropertyMgr> propertyMgrs;

    public PropertyMgrListAdapter(Activity context, List<PropertyMgr> propertyMgrs) {
        super(context, R.layout.layout_propertymgr_item, propertyMgrs);
        this.context = context;
        this.propertyMgrs = propertyMgrs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_propertymgr_item, null, true);

        TextView name = listViewItem.findViewById(R.id.textViewMgrName);

        PropertyMgr propertyMgr = propertyMgrs.get(position);
        name.setText(propertyMgr.getFirstName() + " " + propertyMgr.getLastName());
        //Toast.makeText(context, "Instantiated mgr list!!", Toast.LENGTH_SHORT).show();
        return listViewItem;
    }
}
