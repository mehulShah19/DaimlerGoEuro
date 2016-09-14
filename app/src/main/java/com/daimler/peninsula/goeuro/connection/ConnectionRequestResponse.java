package com.daimler.peninsula.goeuro.connection;


import android.content.Context;
import android.text.TextUtils;

import com.daimler.peninsula.goeuro.R;
import com.daimler.peninsula.goeuro.bean.LocationInformation;
import com.daimler.peninsula.goeuro.utility.GoEuroUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

;

public class ConnectionRequestResponse {

    public synchronized ArrayList<LocationInformation> callLocationAPIService(String textTyped, Context context) {
        ArrayList<LocationInformation> locationInformationArrayList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        if(TextUtils.isEmpty(textTyped)){
            return locationInformationArrayList;
        }
        if (!GoEuroUtils.isInternetAvailable(context)) {
            return locationInformationArrayList;
        }
        try {
            URL url = new URL(context.getString(R.string.locationURL) + GoEuroUtils.getLocale(context) + "/" + textTyped.toLowerCase().trim());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
          //  int responseCode = connection.getResponseCode();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            //Parsing data using Gson Library
            Type listType = new TypeToken<List<LocationInformation>>(){}.getType();
            locationInformationArrayList = (ArrayList<LocationInformation>) new Gson().fromJson(response.toString(), listType);

        }catch(MalformedURLException malfunctionedURLException){
        }catch(IOException ioException){
        }catch(Exception e){
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                }catch (IOException ioException){}
            }
        }
    return locationInformationArrayList;
    }
}