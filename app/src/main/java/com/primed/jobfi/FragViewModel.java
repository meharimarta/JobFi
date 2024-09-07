package com.primed.jobfi;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.content.Context;
import android.app.job.JobScheduler;
import androidx.fragment.app.Fragment;

public class FragViewModel extends ViewModel implements NetworkUtils.OnTaskCompleted {
    private User user;
    private Context context;
    private MutableLiveData<List<Job>> jobList;
    private Fragment host;
    private String url = "http://localhost:8001/api/";
	private AuthStatusCallBack authCallback;
    // ViewModel constructor with parameters
    public FragViewModel(User user, Context ctx, Fragment host) {
        this.context = ctx;
        this.user = user;
        this.host = host;
    }
    
    public LiveData<List<Job>> getJobs() {
        if(jobList == null){ 
           jobList = new MutableLiveData<>();
           loadData();
        }
        return this.jobList;
    }
    
    private void loadData() {
        String userToken = user.getToken();
        NetworkUtils.SendDataTask task = new NetworkUtils.SendDataTask(context, "", this);
        NetworkUtils.url = url + "get-jobs";
        task.setHeader("Authorization", "Bearer "+ userToken);
        task.execute();
    }
    
    @Override
    public void onTaskCompleted(String response)
    {
        List<Job> retrivedJobList = new ArrayList<>();
		JSONObject responseObject = new JSONObject();
        try
        {
	        responseObject = new JSONObject(response);
            JSONArray resData = new JSONArray(responseObject.optString("responseData"));

            for(int i = 0; i < resData.length(); i++) {
                JSONObject job = resData.optJSONObject(i);
                JSONObject fieldOfStudies = new JSONArray(job.optString("field_of_studies")).optJSONObject(0);
                retrivedJobList.add(new Job(job.optString("company"), job.optString("title","Some"), job.optString("deadline"), fieldOfStudies.optString("major", "comming"), job.optString("image", "")));
            }
            jobList.setValue(retrivedJobList);
            Log.d("Home Fragment Job list", jobList.toString());
         //   jobAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
			int resCode = responseObject.optInt("responseCode");
			if(resCode == 401) {
				if(authCallback != null) authCallback.authError();
			}//trigger event
            Log.d("Home Fragment Job list", jobList.toString());
            e.printStackTrace();
        }
    }
    
	public void setAuthCallBack(AuthStatusCallBack callback) {
		this.authCallback = callback;
	}
    public void updateJobList(List<Job> jobs) {
        this.jobList.setValue(jobs);
    }
    
    public User getUser() {
        return user;
    }
    
    public interface ProgressStatus {
        public void viewModelProgressStarted();
        public void viewModelPprogressEnded();
    }
	
	public interface AuthStatusCallBack {
		public void authError();
	}
}
