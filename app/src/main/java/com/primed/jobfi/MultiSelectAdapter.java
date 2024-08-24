package com.primed.jobfi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import androidx.annotation.NonNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiSelectAdapter extends ArrayAdapter<String>
 {
    private List<String> items;
    private Set<String> selectedItems;

    public MultiSelectAdapter(Context context, List<String> items) {
        super(context, android.R.layout.simple_list_item_multiple_choice, items);
        this.items = items;
        this.selectedItems = new HashSet<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout and configure the item view here.
        View view = super.getView(position, convertView, parent);
        CheckedTextView checkedTextView = (CheckedTextView) view;

        String item = items.get(position);
        checkedTextView.setText(item);
        checkedTextView.setChecked(selectedItems.contains(item));

        return view;
    }

  //  @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = getItem(position);

        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        notifyDataSetChanged();
    }

    public Set<String> getSelectedItems() {
        return selectedItems;
    }
}
