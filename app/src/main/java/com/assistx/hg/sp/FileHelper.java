package com.assistx.hg.sp;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
    final static String fileName = "meta.txt";
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    final static String TAG = FileHelper.class.getName();

    public static String ReadFile(Context context) {
        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(path + File.separator + fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {

        }
        return line;
    }

    public static boolean saveToFile(String data) {
        try {
            new File(path).mkdir();
            File file = new File(path + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}