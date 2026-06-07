package motaz.pharaon.adu.ac.ae.traceroute.ui.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;

/*
 * Purpose:
 * - Simple ArrayAdapter for displaying a flat list of EvidenceItems.
 *
 * Note: The MVP handheld UI uses HandheldViewBinder which builds views
 * programmatically. This adapter is retained for any future RecyclerView
 * or ListView integration paths.
 */
public class EvidenceAdapter extends ArrayAdapter<EvidenceItem> {

    public EvidenceAdapter(Context context, List<EvidenceItem> items) {
        super(context, android.R.layout.simple_list_item_2, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        EvidenceItem item = getItem(position);
        if (item == null) {
            return convertView;
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText(item.getTitle());
        text2.setText(item.getCategory() + " // " + item.getSource());

        return convertView;
    }
}
