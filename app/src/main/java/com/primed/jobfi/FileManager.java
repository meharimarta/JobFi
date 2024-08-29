package com.primed.jobfi;
import android.content.Context;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileManager
{
    private static FileManager managerInstance;
    private static Context context;
    private String filename = "user_data.txt";
    public static FileManager manager(Context ctx)
    {
        if (managerInstance == null)
        {
            context = ctx;
            managerInstance = new FileManager();
        }
        return managerInstance;
    }

    public boolean saveFile(String data)
    {

        try
        {
            FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes("UTF-8"));  // Ensure correct encoding
            fos.close();  // Always close streams
        }
        catch (IOException e)
        {
            Log.e("FileUtils", "Error saving JSON data to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getFileDataAsString()
    {
        StringBuilder data = new StringBuilder();
        try
        {

            FileInputStream file = context.openFileInput(filename);
            int ch;
            try
            {
                while ((ch = file.read()) != -1)
                {
                    data.append((char)ch);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "";
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            saveFile("");
            return "";
        }
        return data.toString();
    }
}
