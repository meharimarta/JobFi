package com.primed.jobfi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User
{

    private String name;
    private String email;
    private String experience;
    private String salary;
    private String yearOfGraduation;
    private String token;
    private String fieldOfStudy;
    private int userId;

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private static final String TAG = "####User";

    public User(Context ctx)
    {
        this.context = ctx;
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        loadUserData();
    }

    private void loadUserData()
    {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USER, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
            experience = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPERIENCE));
            salary = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SALARY));
            yearOfGraduation = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_YEAR_OF_GRADUATION));
            token = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOKEN));
            cursor.close();
        }
        else
        {
            Log.d(TAG, "No user data found.");
        }
    }

    public boolean createUser(String name, String email, String experience, String salary, String yearOfGraduation, String token, String fieldOfStudy)
    {
		Log.d(TAG, token);
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		dbHelper.truncateTable(DatabaseHelper.TABLE_USER);
		dbHelper.truncateTable(DatabaseHelper.TABLE_FIELD_OF_STUDY);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_EXPERIENCE, experience);
        values.put(DatabaseHelper.COLUMN_SALARY, salary);
        values.put(DatabaseHelper.COLUMN_YEAR_OF_GRADUATION, yearOfGraduation);
        values.put(DatabaseHelper.COLUMN_TOKEN, token);

        long result = database.insert(DatabaseHelper.TABLE_USER, null, values);
        return result != -1;
    }
    // Insert a new field of study
    public boolean addFieldOfStudy(int majorId, String major)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MAJOR_ID, majorId);
        values.put(DatabaseHelper.COLUMN_MAJOR, major);

        long result = database.insert(DatabaseHelper.TABLE_FIELD_OF_STUDY, null, values);
        return result != -1;
    }

    // Retrieve all fields of study
    public List<JSONObject> getAllFieldsOfStudy()
    {
        List<JSONObject> fieldOfStudyList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_FIELD_OF_STUDY, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do {
                int majorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAJOR_ID));
                String major = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAJOR));
                try
                {
                    fieldOfStudyList.add(new JSONObject("{\"major\":\"" + major + "\",\"major_id\":\"" + majorId + "\" }"));
                }
                catch (JSONException e)
                {}
              //  fieldOfStudyList.add("ID: " + majorId + " - Major: " + major);
            } while (cursor.moveToNext());
            cursor.close();
        }
        else
        {
            Log.d(TAG, "No field of study data found.");
        }
        return fieldOfStudyList;
    }
    public boolean createUserFromJsonString(String jsonString)
	{
		boolean status = true;
		String userName = "";
		String userEmail = "";
		int userId = 0;
		String userToken = "";
		String userExperience = "";
		String userYearOfGraduation = "";
		String userSalary = "";
		JSONArray userFieldOfStudy;

		JSONObject data;
		try
		{
			data = new JSONObject(jsonString);
			Log.d(TAG, "L 126 " + data.toString());
		}
		catch (JSONException e)
		{
			Log.d(TAG, "Unable to parse raw string");
			e.printStackTrace();
			return false;  // Return false if unable to parse the JSON string
		}

		// Ensure data is not null
		if (data != null)
		{
			JSONObject resData;
			try
			{
				resData = new JSONObject(data.optString("responseData"));
			}
			catch (JSONException e)
			{
				Log.d(TAG, "Unable to parse responseData");
				e.printStackTrace();
				return false;  // Return false if responseData parsing fails
			}

			if (resData != null)
			{
				userToken = resData.optString("token", "");

				JSONObject userData = resData.optJSONObject("user");
				if (userData != null)
				{
					userEmail = userData.optString("email", "");
					userName = userData.optString("name", "");
					userId = userData.optInt("id", 0);
				}
				else
				{
					Log.d(TAG, "User data is null");
					return false;  // Return false if user data is missing
				}

				JSONObject infoData = resData.optJSONObject("info");
				if (infoData != null)
				{
					userExperience = infoData.optString("experience", "");
					userYearOfGraduation = infoData.optString("year_of_graduation", "");
					userSalary = infoData.optString("salary", "");
				}
				else
				{
					Log.d(TAG, "Info data is null");
					return false;  // Return false if info data is missing
				}

				// Process field_of_studies
				try
				{
					userFieldOfStudy = new JSONArray(resData.optString("field_of_studies", ""));
				}
				catch (JSONException e)
				{
					Log.d(TAG, "Error parsing field_of_studies");
					e.printStackTrace();
					return false;  // Return false if field_of_studies parsing fails
				}

				if (userFieldOfStudy != null)
				{
					for (int i = 0; i < userFieldOfStudy.length(); i++)
					{
						JSONObject jso = userFieldOfStudy.optJSONObject(i);
						Log.d(TAG +"Field of study :", jso.toString());
						if (jso != null)
						{
							String major = jso.optString("major", "");
							int id = jso.optInt("id", 0);
							addFieldOfStudy(id, major);
						}
					}
				}

				// Create user after all data has been processed
				boolean created = createUser(userName, userEmail, userExperience, userSalary, userYearOfGraduation, userToken, "");
				if (!created)
				{
					Log.d(TAG, "User creation failed");
					return false;  // Return false if user creation fails
				}
			}
			else
			{
				Log.d(TAG, "Response data is null");
				return false;  // Return false if responseData is null
			}
		}
		else
		{
			Log.d(TAG, "Data is null");
			return false;  // Return false if data is null
		}

		return status;
	}

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getToken()
    {
        return token;
    }
    public String getExperience()
    {
        return experience;
    }

    public String getSalary()
    {
        return salary;
    }

    public String getYearOfGraduation()
    {
        return yearOfGraduation;
    }

    public int getId()
    {
        return userId;
    }

    public String getCurrentJob() 
    {
        return "Coder";
    }
    // Add similar getter methods for other fields if needed
}

