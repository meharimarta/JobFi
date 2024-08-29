package com.primed.jobfi;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileUtils {
    private Context context;

    FileUtils(Context context) {
        this.context = context;
    }

    public void saveJsonDataToFile(String jsonData) {
        String filename = "json_data.txt";
        try {
            FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(jsonData.getBytes("UTF-8"));  // Ensure correct encoding
            fos.close();  // Always close streams
        } catch (IOException e) {
            Log.e("FileUtils", "Error saving JSON data to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getJsonDataFromFile() {
		String filename = "json_data.txt";
		StringBuilder stringBuilder = new StringBuilder();
		try {
			FileInputStream fis = this.context.openFileInput(filename);
			int ch;
			while ((ch = fis.read()) != -1) {
				stringBuilder.append((char) ch);
			}
			fis.close();  // Always close streams
		} catch (FileNotFoundException e) {
			Log.e("FileUtils", "File not found, creating a new file.");
			try {
				JSONObject jso = new JSONObject();
				jso.put("data", new JSONArray());
				saveJsonDataToFile(jso.toString());
				return jso.toString();
			} catch (JSONException ex) {
				Log.e("FileUtils", "Error creating new JSON file: " + ex.getMessage());
				ex.printStackTrace();
			}
		} catch (IOException e) {
			Log.e("FileUtils", "Error reading JSON data from file: " + e.getMessage());
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
    
    /**
     * Delete json data
     */
    public boolean deleteData() {
        JSONObject rootObject = new JSONObject();
        try
        {
            rootObject.put("data", new JSONArray());
            saveJsonDataToFile(rootObject.toString());
            return true;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public void appendJsonData(String jsonData) {
        try {
            // Read existing data from file
            String existingData = getJsonDataFromFile();
            JSONObject rootObject;
            JSONArray existingJsonArray;

            if (existingData == null || existingData.isEmpty()) {
                // Create a new root object and JSON array if no data exists
                rootObject = new JSONObject();
                existingJsonArray = new JSONArray();
                rootObject.put("data", existingJsonArray);
            } else {
                // Parse the existing data
                rootObject = new JSONObject(existingData);
                existingJsonArray = rootObject.getJSONArray("data");
            }

            // Parse the new data as a JSONArray
            JSONArray newJsonArray = new JSONArray(jsonData);

            // Append new elements, avoiding duplicates and checking deadlines
            for (int i = 0; i < newJsonArray.length(); i++) {
                JSONObject newJsonObject = newJsonArray.getJSONObject(i);
                if (!containsJsonObject(existingJsonArray, newJsonObject) && isDeadlineValid(newJsonObject)) {
                    existingJsonArray.put(newJsonObject);
                }
            }

            // Save the updated root object with the appended data
            saveJsonDataToFile(rootObject.toString());

        } catch (JSONException e) {
            Log.e("FileUtils", "Error parsing or appending JSON data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean containsJsonObject(JSONArray jsonArray, JSONObject jsonObject) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject existingObject = jsonArray.getJSONObject(i);
                // You can compare based on specific fields, e.g., 'id'
                if (existingObject.toString().equals(jsonObject.toString())) {
                    return true;  // Duplicate found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;  // No duplicate found
    }

    private boolean isDeadlineValid(JSONObject jsonObject) {
        try {
            String deadlineStr = jsonObject.optString("deadLine", null);
            if (deadlineStr == null) {
                return true;  // No deadline means it's always valid
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
            Date deadlineDate = dateFormat.parse(deadlineStr);
            Date currentDate = new Date();

            return !deadlineDate.before(currentDate);

        } catch (ParseException e) {
            Log.e("FileUtils", "Error parsing deadline date: " + e.getMessage());
            e.printStackTrace();
            return true;  // Invalid date format or parsing error
        }
    }
}
