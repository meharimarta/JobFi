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
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.os.Handler;

public class SetupFragment extends Fragment implements NetworkUtils.OnTaskCompleted
{
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;
    private List<FieldOfStudy> fieldOfStudyList;
    private ArrayAdapter<FieldOfStudy> adapter;
    private List<FieldOfStudy> selectedItems;
    private Button submitButton;
    private String url = NetworkUtils.url;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		final View  v = inflater.inflate(R.layout.setup_fragment, container, false);
		
		autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView);
		
        autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView);
        chipGroup =v. findViewById(R.id.chipGroup);
         submitButton = v.findViewById(R.id.save_setup);
        
        selectedItems = new ArrayList<>();
        
		//String[] items = getResources().getStringArray(R.array.item_list);
		fieldOfStudyList = new ArrayList<>();
		fieldOfStudyList.add(new FieldOfStudy("Engineering", 1));
		fieldOfStudyList.add(new FieldOfStudy("Computer", 2));
		fieldOfStudyList.add(new FieldOfStudy("IT and electrical", 3));
		

        // Setup AutoCompleteTextView with custom data
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, fieldOfStudyList);
        autoCompleteTextView.setAdapter(adapter);
        

        // Handle text changes to capture selections
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    // Do nothing
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    String text = s.toString();
                    for (FieldOfStudy item : fieldOfStudyList)
                    {
                        if (item.getField().equalsIgnoreCase(text) && !selectedItems.contains(item))
                        {
                            selectedItems.add(item);
                            addChip(item);
                            autoCompleteTextView.setText("");
                            break;
                        }
                    }
                }
            });
       NetworkUtils.url = "get-field-of-studies";
       
      NetworkUtils.sendDataToServer(getActivity(), "", SetupFragment.this);
       
        setupViews(v);
		return v;
	}
	
    private void setupViews(View v) {
       final EditText nameInput = v.findViewById(R.id.input_name);
       final EditText emailInput = v.findViewById(R.id.input_emial);
       final  EditText inputExperiance = v.findViewById(R.id.input_experiance);
       final EditText currentJobs = v.findViewById(R.id.input_current_job);
        
        submitButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    // url to send data
                    NetworkUtils.url = url + "create-user-data";
                    
                   String name =  nameInput.getText().toString();
                   String email = emailInput.getText().toString();
                   String experiance = inputExperiance.getText().toString();
                   String job = currentJobs.getText().toString();
                   
                    // Prepare data to send to server
                    List<Integer> selectedItemIds = new ArrayList<>();
                    for (FieldOfStudy item : selectedItems)
                    {
                        selectedItemIds.add(item.getId());
                    }
                    
                    ArrayList<JSONObject> data = new ArrayList<>();
                    try {
                           data.add((new JSONObject()).put("email", email));
                           data.add((new JSONObject()).put("name", name));
                           data.add((new JSONObject()).put("experiance", experiance));
                           data.add((new JSONObject()).put("current_job", job));
                           data.add((new JSONObject()).put("fieldOfStudies", selectedItemIds.toString()));
                           
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    NetworkUtils.sendDataToServer(getActivity(), data.toString(), SetupFragment.this);
                  //    UIUtils.showSuccessDialog(getActivity(), "Selected", data.toString());
                    final CustomProgressDialog progressDialog = new CustomProgressDialog(getActivity());
                    progressDialog.show();

                    // Simulate network request
                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // After receiving response
                                progressDialog.showSuccess("Your request was processed successfully!");

                                // Delay the dismissal of the dialog
                                new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                            }
                        }, 3000);
                }
                
            });
    }
    
    private void addChip(final FieldOfStudy item)
    {
        final Chip chip = new Chip(getActivity());
        chip.setText(item.getField());
        chip.setCloseIconVisible(true);
          chip.setOnCloseIconClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                   chipGroup.removeView(chip);
                    selectedItems.remove(item);
               }
         });
        chipGroup.addView(chip);
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
    
    
    @Override
    public void onTaskCompleted(String success)
    {
        UIUtils.showSuccessDialog(getActivity(), "Server response", success);
        // else UIUtils.showErrorDialog(Activity.this, "Failed to send data to server :( is this correct API ? =>" + NetworkUtils.url);
    }
  
    private class FieldOfStudyDataRecived implements NetworkUtils.OnTaskCompleted
    {
        @Override
        public void onTaskCompleted(String response)
        {

        }
    }
}

