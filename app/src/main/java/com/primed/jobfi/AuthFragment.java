package com.primed.jobfi;

import androidx.fragment.app.Fragment;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Button;

public class AuthFragment extends Fragment
{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         //super.onCreateView(inflater, container, savedInstanceState);
         
        final View v = inflater.inflate(R.layout.auth_page, container, false);
        final LinearLayout authBtns = v.findViewById(R.id.auth_buttons);
        final LinearLayout loginView = v.findViewById(R.id.auth_login_view);
         
         loginView.setVisibility(View.GONE);
         
         Button showLoginViewBtn = v.findViewById(R.id.login_btn);
         
        showLoginViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View p1)
                {
                    authBtns.setVisibility(View.GONE);
                    loginView.setVisibility(View.VISIBLE);
                }
         });
         return v;
   }
}
