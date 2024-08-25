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

public class HomeFragment extends Fragment
{
	private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//return super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_fragment, container, false);

		recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		ArrayList<Job> jobList = new ArrayList<>();

		jobList.add(new Job("Coca cola", 
							"October 12, 2024", 
							"Maintainance", 
							"Mechanical Engineering field of study", 
							"https://locgo.com"));

		jobList.add(new Job("Heniken", 
							"October 12, 2024", 
							"Maintainance", 
							"Mechanical Engineering field of study", 
							"https://locgo.com"));
        // Set up the adapter

	    JobAdapter jobAdapter = new JobAdapter(jobList, getContext());
		recyclerView.setAdapter(jobAdapter);

		setupButtons(v);
		return v;
	}

	private void setupButtons(View v)
	{

	}
}
