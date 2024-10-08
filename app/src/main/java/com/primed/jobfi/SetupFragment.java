package com.primed.jobfi;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TextView;

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
    CustomProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		final View  v = inflater.inflate(R.layout.setup_fragment, container, false);
        progressDialog = new CustomProgressDialog(getActivity());
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

        NetworkUtils.sendDataToServer(getContext(), "", new FieldOfStudyDataRecived());

        setupViews(v);

        Log.d(TAG, user.getAllFieldsOfStudy().toString());
        //   List<FieldOfStudy> savedFieldOfStudyList = new ArrayList<>();
        
        for (int i = 0; i < user.getAllFieldsOfStudy().size();  i++)
            {
                JSONObject userD = user.getAllFieldsOfStudy().get(i);
                FieldOfStudy fd = new FieldOfStudy(userD.optString("major"), userD.optInt("id"));
                selectedItems.add(fd);
                addChip(fd);
            }
          yearOfGraduation.add(0, "2000-05-05");
		return v;
	}
    
    final List<String> yearOfGraduation = new ArrayList<>();
    
    private void setupViews(View v)
    {
        final EditText nameInput = v.findViewById(R.id.input_name);
        final EditText emailInput = v.findViewById(R.id.input_emial);
        final EditText inputExperiance = v.findViewById(R.id.input_experiance);
        final EditText inputSalary = v.findViewById(R.id.input_salary);
        final EditText currentJobs = v.findViewById(R.id.input_current_job);
        final EditText inputPassword = v.findViewById(R.id.input_password);
        final TextView inputYearOfGraduation = v.findViewById(R.id.input_year_of_graduation);
        
        if (user.getToken() != null && !user.getToken().isEmpty())
        {
            nameInput.setText(user.getName());
            emailInput.setText(user.getEmail());
            inputExperiance.setText(user.getExperience());
            inputSalary.setText(user.getSalary());
            currentJobs.setText(user.getCurrentJob());
            inputYearOfGraduation.setText(user.getYearOfGraduation());
        }
        
         inputYearOfGraduation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and show it
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            // Handle the selected date here
                            yearOfGraduation.add(0, selectedYear + "-" + (selectedMonth +1) + "-" +selectedDay);
                            inputYearOfGraduation.setText(yearOfGraduation.get(0));
                            // You can now use the selectedDate string
                        }
                    }, year, month, day);

                datePickerDialog.show();
            }
        });
        
        submitButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    // url to send data
                    NetworkUtils.url = "http://localhost:8001/api/create-user";
                   
                    String name =  nameInput.getText().toString();
                    String email = emailInput.getText().toString();
                    int experiance = Integer.parseInt((inputExperiance.getText().toString().isEmpty() ? "0" : inputExperiance.getText().toString()));
                    String job = currentJobs.getText().toString();
                    String password = inputPassword.getText().toString();
                    int salary = Integer.parseInt((inputSalary.getText().toString().isEmpty() ? "0" : inputSalary.getText().toString()));
                    
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
                        userData.put("field_of_studies", selectedItemIds.toString());
                        userData.put("year_of_graduation", yearOfGraduation.get(0));
                        Log.d(TAG, userData.toString());
                        if(user.getToken() != null) {
                            NetworkUtils.url = url + "update-user-data";
                            NetworkUtils.SendDataTask task = new NetworkUtils.SendDataTask(getActivity() ,userData.toString(), SetupFragment.this);
                            
                            task.setHeader("Authorization", "Bearer "+ user.getToken());
                            task.execute();
                        } else {
                        NetworkUtils.sendDataToServer(getContext(), userData.toString(), SetupFragment.this);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    progressDialog.show();
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
        
        if (user.createUserFromJsonString(success))
        {
            progressDialog.showSuccess("Your request was processed successfully!");
            progressDialog.dismiss();
        }
        else
        {
            UIUtils.showErrorDialog(getActivity(), "Internal error", ":(");
        }

        //  UIUtils.showSuccessDialog(getActivity(), "Server response", success);
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
                if (res.getInt("responseCode") == 200) 
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = (new JSONObject(jsonObject.getString("responseData"))).getJSONArray("data");
                    for (int i = 0; i < data.length(); i++)
                    {
                        setupData.add(data.optJSONObject(i));
                    }
                }
                else
                {
                    UIUtils.showErrorDialog(getActivity(), "Network error", "Please connect to.internet");
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

            //UIUtils.showSuccessDialog(getActivity(), "Data updated", response);
        }
    }
}

