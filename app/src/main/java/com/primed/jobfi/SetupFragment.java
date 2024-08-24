package com.primed.jobfi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.chip.Chip;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import com.google.android.material.chip.ChipGroup;

public class SetupFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		final View  v = inflater.inflate(R.layout.setup_fragment, container, false);
		
		AutoCompleteTextView autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView);
		
		String[] items = getResources().getStringArray(R.array.item_list);
		List<String> listItems = Arrays.asList(items);
		
		final MultiSelectAdapter adapter = new MultiSelectAdapter(getActivity(), listItems);
		autoCompleteTextView.setAdapter(adapter);

		autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.onItemClick(parent, view, position, id);
				// Display selected items as chips
				displaySelectedChips(adapter.getSelectedItems());
			}
		});
		return v;
	}
	
	private void displaySelectedChips(final Set<String> selectedItems) {
		ChipGroup chipContainer = getActivity().findViewById(R.id.chipGroup);
		chipContainer.removeAllViews();

		for (final String item : selectedItems) {
			Chip chip = new Chip(getActivity());
			chip.setText(item);
			chip.setCloseIconVisible(true);
			chip.setOnCloseIconClickListener(new View.OnClickListener() {
				public void onClick(View v) {
				selectedItems.remove(item);
				displaySelectedChips(selectedItems);
				}
			});
			chipContainer.addView(chip);
		}
	}
}
