package com.primed.jobfi;
import java.security.PrivateKey;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.UserDataHandler;
import android.content.Context;
import android.util.Log;
import org.json.JSONArray;

public class User
{

    public String name;
    public String email;
    public String experiance;
    public JSONArray fieldOfStudy;
    public String salary;
    public String yearOfGraduation;
    public String currentJob;
    public int userId;
    public String token;
    private Context context;
    String TAG = "####User";
    private FileManager fm;
    public User(Context ctx)
    {
        this.context = ctx;
        fm = new FileManager(context, "user_data.txt");
        JSONObject data = fm.getFileAsJsonObject();
        try
        {
            JSONObject resData = new JSONObject(data.getString("responseData"));
            JSONObject userData = new JSONObject(resData.getString("user"));
            JSONObject infoData = new JSONObject(resData.getString("info"));
            Log.d(TAG, resData.toString());
            Log.d(TAG, userData.toString());
            Log.d(TAG, infoData.toString());
            
            token = resData.getString("token");
            email = userData.getString("email");
            name = userData.getString("name");
            userId = userData.getInt("id");
            experiance = infoData.getString("experiance");
            fieldOfStudy = new JSONArray(resData.getString("field_of_studies"));
            yearOfGraduation = infoData.getString("year_of_graduation");
            salary = infoData.getString("salary");
            
            
        }
        catch (JSONException e)
        { e.printStackTrace();}
    }

    public boolean create(String data)
    {
        if (fm.saveFile(data))
        {
            return true;
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }
}
