package com.primed.jobfi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import java.security.PrivateKey;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HomeFragment extends Fragment implements NetworkUtils.OnTaskCompleted
{
    private String url = "http://localhost:8001/api/";
    private User user;
    List<Job> jobList = new ArrayList<>();
    JobAdapter jobAdapter = new JobAdapter(jobList, getContext());
    public HomeFragment(User user) {
        this.user = user;
    }
    @Override
    public void onTaskCompleted(String response)
    {
        try
        {
            JSONArray resData = new JSONArray((new JSONObject(response).optString("responseData")));
            
            for(int i = 0; i < resData.length(); i++) {
                JSONObject job = resData.optJSONObject(i);
                JSONObject fieldOfStudies = new JSONArray(job.optString("field_of_studies")).optJSONObject(0);
                jobList.add(new Job(job.optString("company"), job.optString("title","Some"), job.optString("deadline"), fieldOfStudies.optString("major", "comming"), job.optString("image", "")));
            }
            Log.d("Home Fragment Job list", jobList.toString());
            jobAdapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            Log.d("Home Fragment Job list", jobList.toString());
            e.printStackTrace();
        }
    }
    
    
	private RecyclerView recyclerView;
   

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_fragment, container, false);

		recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	    
		recyclerView.setAdapter(jobAdapter);

		setupButtons(v);
        getJobs();
		return v;
	}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create the ViewModel using the ViewModelFactory
        FragViewModelFactory factory = new FragViewModelFactory(user, getContext());
        FragViewModel viewModel = new ViewModelProvider(this, factory).get(FragViewModel.class);
    }
    
    
	private void setupButtons(View v)
	{

	}
    
    private void getJobs() {
        String userToken = user.getToken();
        NetworkUtils.SendDataTask task = new NetworkUtils.SendDataTask(getActivity(), "", this);
        NetworkUtils.url = url + "get-jobs";
        task.setHeader("Authorization", "Bearer "+ userToken);
        task.execute();
    }
}

