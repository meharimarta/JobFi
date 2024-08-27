package com.primed.jobfi;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class UIUtils
{
    public static void showSuccessDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    public static void showErrorDialog(Context context,String title, String message) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    public static void showSaveConfirmationDialog(Context context, String title, String msg, final Runnable onConfirm) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(formatJsonResponse(msg))
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onConfirm.run();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    private static String formatJsonResponse(String data) {
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

