package com.primed.jobfi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.primed.jobfi.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>
{

    private List<Job> jobList;
	private MainActivity context;
   private ImageCache imageCache;
    public JobAdapter(List<Job> userList, Context context) {
        imageCache = new ImageCache();
        this.jobList = userList;
		this.context =(MainActivity) context;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
		
        Job job = jobList.get(position);
		
        holder.companyName.setText(job.getCompanyName());
        holder.fieldOfStudy.setText(job.getFieldOfStudy());
		holder.jobPosition.setText(job.getPosition());
		holder.deadLine.setText(job.getDeadLine());
        loadImageIntoImageView(job.getCompanyLogo(), holder.logo);
		
		holder.seeMore.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CustomDialogFragment cdf = CustomDialogFragment.newInstance("Job title", "Job detail");

					cdf.show(context.getSupportFragmentManager(), "Custom dialog");
				}
			});
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
		TextView fieldOfStudy;
		TextView companyName;
		TextView salary;
		TextView deadLine;
		TextView jobPosition;
		ImageView logo;
		Button seeMore;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
			
            fieldOfStudy = itemView.findViewById(R.id.field_of_study);
			companyName = itemView.findViewById(R.id.company_name);
			salary = itemView.findViewById(R.id.salaray);
			deadLine = itemView.findViewById(R.id.dead_line);
			jobPosition = itemView.findViewById(R.id.job_position);
			logo = itemView.findViewById(R.id.company_logo);
			seeMore = itemView.findViewById(R.id.see_more_btn);
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
