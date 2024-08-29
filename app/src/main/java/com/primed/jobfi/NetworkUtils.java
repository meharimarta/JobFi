package com.primed.jobfi;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils
{

    public static String url = "http://localhost:8001/api/"; // Replace with your actual server URL

    public static void sendDataToServer(Context context, String data, OnTaskCompleted callback)
    {
        new SendDataTask(context, data, callback).execute();
    }

    private static class SendDataTask extends AsyncTask<Void, Void, String>
    {

        private String data;
        private OnTaskCompleted callback;

        public SendDataTask(Context context, String data, OnTaskCompleted callback)
        {
            this.data = data;
            this.callback = callback;
        }

        public static void setRoute(String route)
        {
            url = url + route;
        }
        @Override
        protected String doInBackground(Void... voids)
        {
            HttpURLConnection urlConnection = null;
            try
            {
                URL url = new URL(NetworkUtils.url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setDoOutput(true);

                // Send the JSON data to the server
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = data.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response code
                int responseCode = urlConnection.getResponseCode();
                StringBuilder response = new StringBuilder();

                // Check if response is OK
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        String line;
                        while ((line = in.readLine()) != null)
                        {
                            response.append(line);
                        }
                    }
                    return getJsonFormat(response.toString(), responseCode);
                    // return formatJsonResponse(response.toString());
                }
                else
                {
                    // Read error stream for server error responses
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()))) {
                        String line;
                        while ((line = in.readLine()) != null)
                        {
                            response.append(line);
                        }
                    }
                    return getJsonFormat(response.toString(), responseCode);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return getJsonFormat("App error", 0);
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
            }
        }
        protected String getJsonFormat(String response, int code)
        {
            String resData = "0";
            try
            {
                resData = (new JSONObject()).put("responseData", response).put("responseCode", code).toString();
            }
            catch (JSONException e)
            {e.printStackTrace();}
            return resData;
        }
        @Override
        protected void onPostExecute(String result)
        {
            callback.onTaskCompleted(result);
        }

        // Method to format JSON response for readability
        private String formatJsonResponse(String jsonResponse)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.toString(4); // Indent with 4 spaces for better readability
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                return "Error: Unable to parse JSON response.";
            }
        }
    }

    public interface OnTaskCompleted
    {
        void onTaskCompleted(String response);
    }
}

