package com.primed.jobfi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.util.Log;

public class SetupFragment extends Fragment implements NetworkUtils.OnTaskCompleted
{
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;
    private List<FieldOfStudy> fieldOfStudyList;
    private ArrayAdapter<FieldOfStudy> adapter;
    private List<FieldOfStudy> selectedItems;
    private Button submitButton;
    private User user;
    private String url = "http://localhost:8001/api/";
    private String TAG = "***SETUP FRAGMENT";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		final View  v = inflater.inflate(R.layout.setup_fragment, container, false);
       //intiate user instamce
        user = new User(getActivity());
        autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView);
        chipGroup = v. findViewById(R.id.chipGroup);
        submitButton = v.findViewById(R.id.save_setup);

        selectedItems = new ArrayList<>();

		//String[] items = getResources().getStringArray(R.array.item_list);
		 fieldOfStudyList = new ArrayList<>();
         
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
        NetworkUtils.url = url + "get-setup-data";

        NetworkUtils.sendDataToServer(getActivity(), "", new FieldOfStudyDataRecived());

        setupViews(v);
       
        Log.d(TAG, user.fieldOfStudy.optJSONObject(0).toString());
        //   List<FieldOfStudy> savedFieldOfStudyList = new ArrayList<>();
        try {
            for(int i = 0; i < user.fieldOfStudy.length(); i++)
            {
                JSONObject userD = user.fieldOfStudy.optJSONObject(i);
                addChip(new FieldOfStudy(userD.getString("major"), userD.getInt("id")));
            }
        } catch(JSONException e) {

        }
		return v;
	}

    private void setupViews(View v)
    {
        final EditText nameInput = v.findViewById(R.id.input_name);
        final EditText emailInput = v.findViewById(R.id.input_emial);
        final  EditText inputExperiance = v.findViewById(R.id.input_experiance);
        final  EditText inputSalary = v.findViewById(R.id.input_salary);
        final EditText currentJobs = v.findViewById(R.id.input_current_job);
        final EditText inputPassword = v.findViewById(R.id.input_password);
        
        if(user.token != null && !user.token.isEmpty()) {
            nameInput.setText(user.name);
            emailInput.setText(user.email);
            inputExperiance.setText(user.experiance);
            inputSalary.setText(user.salary);
            currentJobs.setText(user.currentJob);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    // url to send data
                    NetworkUtils.url = "http://localhost:8001/api/create-user";

                    String name =  nameInput.getText().toString();
                    String email = emailInput.getText().toString();
                    String experiance = inputExperiance.getText().toString();
                    String job = currentJobs.getText().toString();
                    String password = inputPassword.getText().toString();
                    String salary = inputSalary.getText().toString();
                    // Prepare data to send to server
                    List<Integer> selectedItemIds = new ArrayList<>();
                    for (FieldOfStudy item : selectedItems)
                    {
                        selectedItemIds.add(item.getId());
                    }
                    try
                    {
                        JSONObject userData = new JSONObject();
                        userData.put("email", email);
                        userData.put("name", name);
                        userData.put("password", password);
                        userData.put("experiance", experiance);
                        userData.put("current_job", job);
                        userData.put("salary", salary);
                        userData.put("field_of_studies", selectedItemIds);
                        
                        NetworkUtils.sendDataToServer(getActivity(), userData.toString(), SetupFragment.this);
                        
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                       //    UIUtils.showSuccessDialog(getActivity(), "Selected", data.toString());
                    final CustomProgressDialog progressDialog = new CustomProgressDialog(getActivity());
                    progressDialog.show();

                    // Simulate network request
                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run()
                            {
                                // After receiving response
                                progressDialog.showSuccess("Your request was processed successfully!");

                                // Delay the dismissal of the dialog
                                new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run()
                                        {
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
                public void onClick(View v)
                {
                    chipGroup.removeView(chip);
                    selectedItems.remove(item);
                }
            });
        chipGroup.addView(chip);
    }

	private void displaySelectedChips(final Set<String> selectedItems)
    {
		ChipGroup chipContainer = getActivity().findViewById(R.id.chipGroup);
		chipContainer.removeAllViews();

		for (final String item : selectedItems)
        {
			Chip chip = new Chip(getActivity());
			chip.setText(item);
			chip.setCloseIconVisible(true);
			chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
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
        FileManager fm = new FileManager(getContext(), "user_data.txt");
        if(fm.saveFile(success)) {
            
        }
        UIUtils.showSuccessDialog(getActivity(), "Server response", success);
    }

    private class FieldOfStudyDataRecived implements NetworkUtils.OnTaskCompleted
    {
        @Override
        public void onTaskCompleted(String response)
        {
            ArrayList<JSONObject> setupData = new ArrayList<>();
            try
            {
                JSONObject res = new JSONObject(response);
                Log.d(TAG, response);
                if(res.getInt("responseCode") == 200) 
                {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray data = (new JSONObject(jsonObject.getString("responseData"))).getJSONArray("data");
                for (int i = 0; i < data.length(); i++)
                {
                    setupData.add(data.optJSONObject(i));
                }
               }else {
                   UIUtils.showErrorDialog(getActivity(), "Network error","Please connect to.internet");
                   return;
               }
            }
            catch (JSONException e)
            {
                UIUtils.showErrorDialog(getActivity(), "Internal error", ":(");
                e.printStackTrace();
            }

            try
            {
                for (int i = 0; i < setupData.size(); i++)
                {
                    fieldOfStudyList.add(new FieldOfStudy(setupData.get(i).getString("major"), setupData.get(i).getInt("id")));
                }
            }
            catch (JSONException e)
            {
                Log.d(TAG, "Error creating fieldOfStuyList  new FieldOfStrudy()");
                e.printStackTrace();
            }
            Log.d(TAG, fieldOfStudyList.toString());
          //  adapter.clear();
            adapter.addAll(fieldOfStudyList);
            adapter.notifyDataSetChanged();
            
           UIUtils.showSuccessDialog(getActivity(), "Data updated", response);
        }
    }
}

