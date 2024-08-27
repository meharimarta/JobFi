package com.primed.jobfi;
import android.text.SpannableStringBuilder;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.text.Spanned;

public class Job
{
	private String companyName;
	private String jobPosition;
	private String fieldOfStudy;
	private String deadLine;
	private String logo;
	
	public Job(String companyName, String position, String deadline, String fieldOfStudy, String logo) {
		this.companyName = styleLabelBold(companyName, "Company: ");
		this.jobPosition = styleLabelBold(position, "Position: ");
		this.deadLine = styleLabelBold(deadline, "Deadline: ");
		this.fieldOfStudy = styleLabelBold(fieldOfStudy, "Field of study: ");
		this.logo = logo;
	}
	
	private String styleLabelBold(String text, String label) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();
		SpannableString labelSpan  = new SpannableString(label);
		labelSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		spannable.append(labelSpan);
		
		spannable.append(text);
		
		return spannable.toString();
	}
	
	public String getCompanyName()
	{
		return this.companyName;
	}
	
	public String getFieldOfStudy()
	{
		return this.fieldOfStudy;
	}
	
	public String getPosition()
	{
		return this.jobPosition;
	}
	
	public String getDeadLine()
	{
		return this.deadLine;
	}
}
