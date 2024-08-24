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

        // Initialize User List
        userList = new ArrayList<>();
        userList.add(new User("John Doe", "john.doe@example.com"));
        userList.add(new User("Jane Doe", "jane.doe@example.com"));

        // Set up the adapter
        userAdapter = new UserAdapter(userList, getContext());
        recyclerView.setAdapter(userAdapter);
		setupButtons(v);
		return v;
	}
	
	private void setupButtons(View v) {
		
	}
}
