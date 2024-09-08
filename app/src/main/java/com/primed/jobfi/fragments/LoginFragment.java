package com.primed.jobfi.fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import com.primed.jobfi.R;
import android.widget.EditText;
import com.primed.jobfi.NetworkUtils;
import com.primed.jobfi.MainActivity;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;
import com.primed.jobfi.CustomProgressDialog;
import android.widget.Button;
import android.widget.ProgressBar;
import com.primed.jobfi.User;
import com.primed.jobfi.HomeFragment;

public class LoginFragment extends Fragment implements NetworkUtils.OnTaskCompleted
{

	private String TAG = "LoginFragment";

	private CustomProgressDialog progress;

	private Button makeLoginBtn;
    private ProgressBar btnProgressBar;
	@Override
	public void onTaskCompleted(String response)
	{
		enableLoginBtn();
		try
		{
		    JSONObject data = new JSONObject(response);
			Log.d(TAG, data.toString());
			int resCode = data.optInt("responseCode");
			if(resCode == 200) {
				User user = new User(getActivity());
				if(user.createUserFromJsonString(response)) {
					user.loadUserData();
					((MainActivity)getActivity()).replaceFragment(new HomeFragment(user));
				}
				
			} else if(resCode == 422 ) {
				progress.showSuccess("Hey Your email or password is not correct");
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	private void disableLoginBtn()
	{
		makeLoginBtn.setText("");
		makeLoginBtn.setEnabled(false);
		btnProgressBar.setVisibility(View.VISIBLE);
	}
	
	private void enableLoginBtn()
	{
		makeLoginBtn.setText("LOG IN");
		makeLoginBtn.setEnabled(true);
		btnProgressBar.setVisibility(View.GONE);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.layout_login, container, false);
		
		progress = new CustomProgressDialog(getActivity());
		
		
		final EditText inputEmail = v.findViewById(R.id.input_emaill);
		final EditText inputPassword = v.findViewById(R.id.input_password);
		
		
	   makeLoginBtn = v.findViewById(R.id.make_login);
	   btnProgressBar = v.findViewById(R.id.btn_progress_bar);
	   final LoginFragment callabck = this;
		makeLoginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				final JSONObject data = new JSONObject();

				try
				{
					data.put("email", email);
					data.put("password", password);
				} catch(JSONException e) {
					e.printStackTrace();
				}
				Log.d(TAG, data.toString());
				
				disableLoginBtn();
				NetworkUtils.url = "http://localhost:8001/api/login";
				NetworkUtils.sendDataToServer(getActivity(), data.toString(), callabck);
	      }
		});
        return v;
   }
}
