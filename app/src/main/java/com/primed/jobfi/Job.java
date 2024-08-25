package com.primed.jobfi;

public class Job
{
	private String companyName;
	private String jobPosition;
	private String fieldOfStudy;
	private String deadLine;
	private String logo;
	
	public Job(String companyName, String position, String deadline, String fieldOfStudy, String logo) {
		this.companyName = companyName;
		this.jobPosition = position;
		this.deadLine = deadline;
		this.fieldOfStudy = fieldOfStudy;
		this.logo = logo;
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
