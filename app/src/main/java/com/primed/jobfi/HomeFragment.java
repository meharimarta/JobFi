package com.primed.jobfi;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment // implements NetworkUtils.OnTaskCompleted //FragViewModel.ProgressStatus
{
    private User user;
    List<Job> jobList = new ArrayList<>();
    private FragViewModel fragViewModel;

    JobAdapter jobAdapter;

    private CustomProgressDialog progressDialog;
    public HomeFragment() {}
    public HomeFragment(User user)
    {
        this.user = user;
    }

	private RecyclerView recyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_fragment, container, false);
        jobAdapter = new JobAdapter(jobList, getContext());
        
        progressDialog = new CustomProgressDialog(getActivity());
        
		recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		recyclerView.setAdapter(jobAdapter);

        FragViewModelFactory factory = new FragViewModelFactory(user, getActivity(), this);
        fragViewModel = new ViewModelProvider(this, factory).get(FragViewModel.class);

        fragViewModel.getJobs().observe(getViewLifecycleOwner(), new Observer<List<Job>>() {
                @Override
                public void onChanged(List<Job> jobs)
                {
                    jobAdapter.updateData(jobs);
                    progressDialog.hide();
                }
            });
        progressDialog.setDialogTitle("Searching your jobs 💸");
        progressDialog.setDialogInfo("Your setup match jobs will be avialable ....");
        progressDialog.show();
		return v;
	}
}

