package com.primed.jobfi;

public class FieldOfStudy
{
	String field;
	int id;
	
	public FieldOfStudy(String field, int id) 
	{
		this.field = field;
		this.id = id;
	}
	
	public String getField() {
		return field;
	}
	
    @Override
    public String toString() {
        return field;
    }
	public int getId() {
		return id;
	}
}
