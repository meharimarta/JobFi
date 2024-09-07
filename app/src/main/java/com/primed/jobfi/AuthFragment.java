package com.primed.jobfi;

import androidx.fragment.app.Fragment;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Button;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import com.primed.jobfi.fragments.LoginFragment;

public class AuthFragment extends Fragment
{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         //super.onCreateView(inflater, container, savedInstanceState);
         
        final View v = inflater.inflate(R.layout.auth_page, container, false);
         Button showLoginViewBtn = v.findViewById(R.id.login_btn);
         
        showLoginViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View p1)
                {
                 ((MainActivity) getActivity()).replaceFragment(new LoginFragment());
                }
         });
         
		 Button signupBtn = v.findViewById(R.id.signup_btn);
		 
		 signupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			 public void onClick(View v) {
				 ((MainActivity) getActivity()).replaceFragment(new SetupFragment());
			 }
		 });
         
         return v;
   }
}
