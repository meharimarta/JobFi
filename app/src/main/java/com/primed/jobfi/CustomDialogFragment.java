package com.primed.jobfi;

import android.graphics.Bitmap;
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
    private static ImageCache imageCache;
    private static Job job;
    public static CustomDialogFragment newInstance(Job j) {
        job = j;
        imageCache = new ImageCache();
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", job.getCompanyName());
        args.putString("message", job.getFieldOfStudy());
        
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

        loadImageIntoImageView(job.getCompanyLogo(), dialogImageView);
        
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
    
    public void loadImageIntoImageView(final String urlString, final ImageView imageView) {
        Bitmap cachedBitmap = imageCache.getBitmapFromMemCache(urlString);
        if (cachedBitmap != null) {
            // Load the image from cache
            imageView.setImageBitmap(cachedBitmap);
        } else {
            // Download the image and cache it
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = ImageDownloader.downloadImage(urlString);
                        if (bitmap != null) {
                            imageCache.addBitmapToMemoryCache(urlString, bitmap);
                            imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                        }
                    }
                }).start();
        }
    }
}
