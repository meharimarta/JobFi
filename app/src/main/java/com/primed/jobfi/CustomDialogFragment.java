package com.primed.jobfi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    public static CustomDialogFragment newInstance(String title, String message) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_dialog, container, false);

        ImageView dialogImageView = view.findViewById(R.id.dialogImageView);
        TextView dialogTextView = view.findViewById(R.id.dialogTextView);
        Button negativeButton = view.findViewById(R.id.negativeButton);
        Button positiveButton = view.findViewById(R.id.positiveButton);

        if (getArguments() != null) {
            dialogTextView.setText(getArguments().getString("message"));
        }

        negativeButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

        positiveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
