package hk.edu.ouhk.comp313f_groupproject;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherInfo {
    public static String TIMEZONE = "timezone";
    public static String TIMEZONE_OFFSET = "timezone_offset";

    public static String TEMP = "temp";
    public static String FEELS_LIKE = "feels_like";
    public static String PRESSURE = "pressure";
    public static String HUMIDITY = "humidity";
    public static String VISIBILITY = "visibility";
    public static String WIND_SPEED = "wind_speed";
    public static String WIND_DEG = "wind_deg";
    public static String WEATHER = "weather";
    public static String DESCRIPTION = "description";
    public static String ICON = "icon";
    public static String RAIN = "rain";
    public static String DAY = "day";
    public static String MIN = "min";
    public static String MAX = "max";
    public static String NIGHT = "night";
    public static String EVE = "eve";
    public static String MORN = "morn";

    public static ArrayList<HashMap<String, String>> weatherList = new ArrayList<>();

    // Creates and add contact to contact list
    public static void addWeather(String timezone, Integer timezone_offset, Integer temp, Integer feels_like, Integer pressure,
                                  Integer humidity, Integer visibility, Integer wind_speed, Integer wind_deg, String weather,
                                  String description, String icon, Double rain) {
        // Create contact
        HashMap<String, String> weathers = new HashMap<>();
        weathers.put(TIMEZONE, timezone);
        weathers.put(TIMEZONE_OFFSET, timezone_offset.toString());

        weathers.put(TEMP, temp.toString());
        weathers.put(FEELS_LIKE, feels_like.toString());
        weathers.put(PRESSURE, pressure.toString());
        weathers.put(HUMIDITY, humidity.toString());
        weathers.put(VISIBILITY, visibility.toString());
        weathers.put(WIND_SPEED, wind_speed.toString());
        weathers.put(WIND_DEG, wind_deg.toString());
        weathers.put(WEATHER, weather);
        weathers.put(DESCRIPTION, description);
        weathers.put(ICON, icon);
        weathers.put(RAIN, rain.toString());

        // Add contact to contact list
        weatherList.add(weathers);
    }

    public static void addDailyWeather(Integer day, Integer min, Integer max, Integer night, Integer eve, Integer morn) {
        // Create contact
        HashMap<String, String> weathers = new HashMap<>();
        weathers.put(DAY, day.toString());
        weathers.put(MIN, min.toString());;
        weathers.put(MAX, max.toString());
        weathers.put(NIGHT, night.toString());
        weathers.put(EVE, eve.toString());
        weathers.put(MORN, morn.toString());

        // Add contact to contact list
        weatherList.add(weathers);
    }

    public static void addCityWeather(){
        HashMap<String, String> weathers = new HashMap<>();




    }

}
