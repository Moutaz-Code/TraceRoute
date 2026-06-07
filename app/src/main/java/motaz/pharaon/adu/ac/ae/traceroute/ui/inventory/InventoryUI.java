package motaz.pharaon.adu.ac.ae.traceroute.ui.inventory;

import android.content.Context;
import android.widget.ListView;

import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;

/*
 * Purpose:
 * - Legacy inventory view helper. Retained as a compile target.
 *
 * The active evidence display path for the MVP is HandheldViewBinder.
 * This class is not wired into any current Fragment.
 */
public class InventoryUI {

    private final ListView listView;
    private EvidenceAdapter adapter;

    public InventoryUI(Context context, ListView listView) {
        this.listView = listView;
    }

    public void bind(List<EvidenceItem> items) {
        if (adapter == null) {
            adapter = new EvidenceAdapter(listView.getContext(), items);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    public void refresh(List<EvidenceItem> items) {
        bind(items);
    }
}
