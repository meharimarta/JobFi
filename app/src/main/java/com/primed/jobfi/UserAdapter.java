package com.primed.jobfi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.primed.jobfi.R;
import com.primed.jobfi.UserAdapter;
import java.util.List;
import android.widget.Button;
import android.content.Context;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>
 {

    private List<User> userList;
	private MainActivity context;
    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
		this.context =(MainActivity) context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());
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
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
		Button seeMore;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
           // nameTextView = itemView.findViewById(R.id.nameTextView);
          //  emailTextView = itemView.findViewById(R.id.emailTextView);
			seeMore = itemView.findViewById(R.id.see_more_btn);
        }
    }
}
