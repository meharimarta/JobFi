package com.primed.jobfi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONFormatter
{
    private static JSONFormatter formatterInstance;
    
    public static JSONFormatter getInstance() {
        if(formatterInstance == null)
        {
            formatterInstance = new JSONFormatter();
        }
        
        return formatterInstance;
    }
    
    private JSONFormatter() {}
    
    public static String formatJsonResponse(String data) {
        if (data.trim().startsWith("[")) {
            try {
                JSONObject  rootObject = new JSONObject();
                JSONArray  existingJsonArray = new JSONArray();
                rootObject.put("data", existingJsonArray);

                JSONArray newJsonArray = new JSONArray(data);

                for (int i = 0; i < newJsonArray.length(); i++) {
                    JSONObject newJsonObject = newJsonArray.getJSONObject(i);
                    System.out.println(newJsonObject);
                    // Check for duplicates and valid deadline before adding

                    existingJsonArray.put(newJsonObject);
                }
                return rootObject.toString();
            } catch ( JSONException e) {
                e.printStackTrace();
                return "Unable parse raw json array object";
            }
        }else {
            try {
                JSONObject jsonObject = new JSONObject(data);
                return jsonObject.toString(4); // Indent with 4 spaces for better readability
            } catch (JSONException e) {
                e.printStackTrace();
                //return it as is cz it is plain string
                return data;
            }
        }
    }
}
