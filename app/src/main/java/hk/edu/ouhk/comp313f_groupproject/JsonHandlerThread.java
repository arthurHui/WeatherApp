package hk.edu.ouhk.comp313f_groupproject;

import android.util.Log;

import androidx.constraintlayout.motion.widget.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JsonHandlerThread extends Thread {
    private static final String TAG = "JsonHandlerThread";
    // URL to get contacts JSON file
    private static String jsonUrl = "";
    private static boolean daily = false;
    private static boolean week = false;
    private static boolean city = false;

    public void setURL(String urlStr) {
        jsonUrl = urlStr;
    }

    public void setDaily(Boolean tf) {
        daily = tf;
    }

    public void setCity(Boolean tf) { city = tf;}

    public void setweek(Boolean tf) {
        week = tf;
    }

    public static String makeRequest() {
        String response = null;
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = inputStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private static String inputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
        return sb.toString();
    }

    public void run() {
        String weatherStr = makeRequest();
        Log.e(TAG, "Response from url: " + weatherStr);

        if (weatherStr != null) {
            if (city == true) {
                try{
                    JSONObject jsonObj = new JSONObject(weatherStr);

                    Integer timezone_offset = jsonObj.getInt("timezone");
                    JSONObject sys = jsonObj.getJSONObject("sys");
                    String timezone = sys.getString("country");

                    JSONObject current = jsonObj.getJSONObject("main");
                    Integer temp = current.getInt("temp");
                    Integer feels_like = current.getInt("feels_like");
                    Integer pressure = current.getInt("pressure");
                    Integer visibility = jsonObj.getInt("visibility");
                    Integer humidity = current.getInt("humidity");


                    JSONArray weatherA = jsonObj.getJSONArray("weather");
                    JSONObject weatherO = weatherA.getJSONObject(0);
                    String weather = weatherO.getString("main");
                    String description = weatherO.getString("description");
                    String icon = weatherO.getString("icon");

                    JSONObject wind = jsonObj.getJSONObject("wind");
                    Integer wind_speed = wind.getInt("speed");
                    Integer wind_deg = wind.getInt("deg");

                    Log.e(TAG, "if ");
                    if (current.has("rain")) {
                        JSONObject rainO = current.getJSONObject("rain");
                        Double rain = rainO.getDouble("1h");
                        WeatherInfo.addWeather(timezone, timezone_offset, temp, feels_like, pressure, humidity, visibility, wind_speed,
                                wind_deg, weather, description, icon, rain);
                    } else {
                        WeatherInfo.addWeather(timezone, timezone_offset, temp, feels_like, pressure, humidity, visibility, wind_speed,
                                wind_deg, weather, description, icon, 0.0);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            }else {
                try {
                    JSONObject jsonObj = new JSONObject(weatherStr);

                    String timezone = jsonObj.getString("timezone");
                    Integer timezone_offset = jsonObj.getInt("timezone_offset");

                    JSONObject current = jsonObj.getJSONObject("current");
                    Integer temp = current.getInt("temp");
                    Integer feels_like = current.getInt("feels_like");
                    Integer pressure = current.getInt("pressure");
                    Integer humidity = current.getInt("humidity");
                    Integer visibility = current.getInt("visibility");
                    Integer wind_speed = current.getInt("wind_speed");
                    Integer wind_deg = current.getInt("wind_deg");

                    JSONArray weatherA = current.getJSONArray("weather");
                    JSONObject weatherO = weatherA.getJSONObject(0);
                    String weather = weatherO.getString("main");
                    String description = weatherO.getString("description");
                    String icon = weatherO.getString("icon");

                    if(daily == true){

                        JSONArray dailyA = jsonObj.getJSONArray("daily");
                        JSONObject dailyO = dailyA.getJSONObject(0);
                        JSONObject dailytemp = dailyO.getJSONObject("temp");
                        Integer day = dailytemp.getInt("day");
                        Integer min = dailytemp.getInt("min");
                        Integer max = dailytemp.getInt("max");
                        Integer night = dailytemp.getInt("night");
                        Integer eve = dailytemp.getInt("eve");
                        Integer morn = dailytemp.getInt("morn");
                        WeatherInfo.addDailyWeather(day, min, max, night, eve, morn);
                        daily = false;
                    }else if(week == true){
                        for(int i = 1; i<=7;i++){
                            JSONArray dailyA = jsonObj.getJSONArray("daily");
                            JSONObject dailyO = dailyA.getJSONObject(i);
                            JSONObject dailytemp = dailyO.getJSONObject("temp");
                            Integer day = dailytemp.getInt("day");
                            Integer min = dailytemp.getInt("min");
                            Integer max = dailytemp.getInt("max");
                            Integer night = dailytemp.getInt("night");
                            Integer eve = dailytemp.getInt("eve");
                            Integer morn = dailytemp.getInt("morn");
                            WeatherInfo.addDailyWeather(day, min, max, night, eve, morn);
                        }
                        week = false;}
                    else {
                        if (current.has("rain")) {
                            JSONObject rainO = current.getJSONObject("rain");
                            Double rain = rainO.getDouble("1h");
                            WeatherInfo.addWeather(timezone, timezone_offset, temp, feels_like, pressure, humidity, visibility, wind_speed,
                                    wind_deg, weather, description, icon, rain);
                        } else {
                            WeatherInfo.addWeather(timezone, timezone_offset, temp, feels_like, pressure, humidity, visibility, wind_speed,
                                    wind_deg, weather, description, icon, 0.0);
                        }
                    }


                    // Add contact (name, email, address) to contact list

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }
}

