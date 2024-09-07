package com.primed.jobfi;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import android.view.View;

public class CustomProgressDialog extends Dialog {

    private TextView dialogTitle;
    private ProgressBar progressBar;
    private TextView progressInfo;
    private ImageView successIcon;

    public CustomProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.progress_dialoge);

        dialogTitle = findViewById(R.id.dialogTitle);
        progressBar = findViewById(R.id.progressBar);
        progressInfo = findViewById(R.id.progressInfo);
        successIcon = new ImageView(context);

        // Configure Success Icon (hidden by default)
        successIcon.setVisibility(View.GONE);
        successIcon.setImageResource(R.drawable.check_icon); // Replace with your check icon resource
    }

    public void updateProgressInfo(String info) {
        progressInfo.setText(info);
    }

    public void showSuccess(String successMessage) {
        progressBar.setVisibility(View.GONE);
        successIcon.setVisibility(View.VISIBLE);
        dialogTitle.setText("Success");
        updateProgressInfo(successMessage);
    }
    
    public void setDialogTitle(String title) {
        this.dialogTitle.setText(title);
    }
    
    public void setDialogInfo(String info) {
        this.progressInfo.setText(info);
    }
    private interface ProgressStatus {
        public void progressStarted();
        public void progressEnded();
    }
}
