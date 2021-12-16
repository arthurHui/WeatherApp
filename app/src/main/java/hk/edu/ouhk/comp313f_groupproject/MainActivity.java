package hk.edu.ouhk.comp313f_groupproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.se.omapi.Session;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    static String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private Button button;
    private EditText lat;
    private EditText lon;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText email;
    private Button emailbtn;
    private double emaillat;
    private double emaillon;
    private String msg;
    private Button dailybtn;
    private AlertDialog.Builder dialog;
    private Button currentbutton;
    private Button weekbtn;
    Button citybutton;
    EditText city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        button = findViewById(R.id.enterbutton);
        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);
        email = findViewById(R.id.email);
        emailbtn = findViewById(R.id.emailbutton);
        dailybtn = findViewById(R.id.dailybtn);
        dialog = new AlertDialog.Builder(MainActivity.this);
        currentbutton = findViewById(R.id.currentbutton);
        citybutton = findViewById(R.id.citybutton);
        city = findViewById(R.id.city);
        weekbtn = findViewById(R.id.weekbtn);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (lat.getText().toString().equals("") || lat.getText().toString().startsWith("+") || Integer.parseInt(lat.getText().toString()) < -90 || Integer.parseInt(lat.getText().toString()) > 90) {
                    lat.setError("Please enter a valid latitude number");
                    if (lon.getText().toString().equals("") || lon.getText().toString().startsWith("+") || Integer.parseInt(lon.getText().toString()) < -180 || Integer.parseInt(lon.getText().toString()) > 180) {
                        lon.setError("Please enter a valid longitude number");
                    }
                }else if(lon.getText().toString().equals("") || lon.getText().toString().startsWith("+") || Integer.parseInt(lon.getText().toString()) < -180 && Integer.parseInt(lon.getText().toString()) > 180) {
                    lon.setError("Please enter a valid longitude number");
                    if (lat.getText().toString().equals("") || lat.getText().toString().startsWith("+") || Integer.parseInt(lat.getText().toString()) < -90 && Integer.parseInt(lat.getText().toString()) > 90) {
                        lat.setError("Please enter a valid latitude number");
                    }
                }else{
                    String urlStr = "https://api.openweathermap.org/data/2.5/onecall?lat=" + Integer.parseInt(lat.getText().toString()) + "&lon=" + Integer.parseInt(lon.getText().toString()) + "&appid=b8c6543311514d9c5e62992c10054122" + "&units=metric";
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, urlStr);
                    startActivity(intent);
                }

            }
        });

        weekbtn.setOnClickListener((v -> {
            String urlStr="https://api.openweathermap.org/data/2.5/onecall?lat=" +emaillat+ "&lon=" +emaillon+ "&appid=b8c6543311514d9c5e62992c10054122"+"&units=metric";
            JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
            jsonHandlerThread.setURL(urlStr);
            jsonHandlerThread.setweek(true);
            jsonHandlerThread.start();
            try {
                jsonHandlerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String dia ="";
            int x=0;
            for(int i = 6;i>=0;i--){
                String day = WeatherInfo.weatherList.get(i).get("day");
                String min = WeatherInfo.weatherList.get(i).get("min");
                String max = WeatherInfo.weatherList.get(i).get("max");
                String night = WeatherInfo.weatherList.get(i).get("night");
                String eve = WeatherInfo.weatherList.get(i).get("eve");
                String morn = WeatherInfo.weatherList.get(i).get("morn");
                x++;

                dia += "Day"+x+":\n"+
                        "average temp"+day+ "\n" +
                        "min temp: " + min + "\n"+
                        "max temp: " + max + "\n"+
                        "night temp: " + night + "\n"+
                        "eve temp: " + eve + "\n"+
                        "morn temp: " + morn+ "\n";
            }

            dialog.setTitle("Daily weather Info");
            dialog.setMessage(dia);
            dialog.show();
            Log.e("week",WeatherInfo.weatherList.toString());
            for(int i = 6;i>=0;i--) {
                WeatherInfo.weatherList.remove(i);
            }
        }));

        citybutton.setOnClickListener((view -> {

            String urlStr="https://api.openweathermap.org/data/2.5/weather?q="+city.getText()+"&appid=492abc4c55f90c8e7e57845f97bd3d13"+"&units=metric";
            Log.e(TAG, "Response from url: " + urlStr);

            JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
            jsonHandlerThread.setURL(urlStr);
            jsonHandlerThread.setCity(true);
            jsonHandlerThread.start();
            try {
                jsonHandlerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String temp = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("temp");
            String feels_like = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("feels_like");
            String pressure = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("pressure");
            String humidity = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("humidity");
            String visibility = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("visibility");
            String wind_speed = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("wind_speed");
            String wind_deg = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("wind_deg");
            String weather = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("weather");
            String description = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("description");
            String rain = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("rain");
            String icon = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("icon");

            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra("TEMP", temp);
            intent.putExtra("FEELS_LIKE", feels_like);
            intent.putExtra("PRESSURE", pressure);
            intent.putExtra("HUMIDITY", humidity);
            intent.putExtra("VISIBILITY", visibility);
            intent.putExtra("WIND_SPEED", wind_speed);
            intent.putExtra("WIND_DEG", wind_deg);
            intent.putExtra("WEATHER", weather);
            intent.putExtra("DESCRIPTION", description);
            intent.putExtra("ICON", icon);
            intent.putExtra("RAIN", rain);
            startActivity(intent);
        }));

        currentbutton.setOnClickListener((view -> {
            String urlStr="https://api.openweathermap.org/data/2.5/onecall?lat=" +emaillat+ "&lon=" +emaillon+ "&appid=b8c6543311514d9c5e62992c10054122"+"&units=metric";
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra(EXTRA_MESSAGE, urlStr);
            startActivity(intent);

        }));

        dailybtn.setOnClickListener((v -> {
                String urlStr="https://api.openweathermap.org/data/2.5/onecall?lat=" +emaillat+ "&lon=" +emaillon+ "&appid=b8c6543311514d9c5e62992c10054122"+"&units=metric";
                JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
                jsonHandlerThread.setURL(urlStr);
                jsonHandlerThread.setDaily(true);
                jsonHandlerThread.start();
                try {
                    jsonHandlerThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String day = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("day");
                String min = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("min");
                String max = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("max");
                String night = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("night");
                String eve = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("eve");
                String morn = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("morn");

                dialog.setTitle("Daily weather Info");
                dialog.setMessage("Today average temp: " + day + "\n" +
                                  "Today min temp: " + min + "\n"+
                                  "Today max temp: " + max + "\n"+
                                  "Today night temp: " + night + "\n"+
                                  "Today eve temp: " + eve + "\n"+
                                  "Today morn temp: " + morn);
                dialog.show();
                Log.e("TAG",WeatherInfo.weatherList.toString());
                WeatherInfo.weatherList.remove(WeatherInfo.weatherList.size()-1);
        }));

        emailbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(eamilCheck()){
                    String urlStr="https://api.openweathermap.org/data/2.5/onecall?lat=" +emaillat+ "&lon=" +emaillon+ "&appid=b8c6543311514d9c5e62992c10054122"+"&units=metric";
                    JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
                    jsonHandlerThread.start();
                    jsonHandlerThread.setURL(urlStr);
                    try {
                        jsonHandlerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String temp = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("temp");
                    String feels_like = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("feels_like");
                    String pressure = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("pressure");
                    String humidity = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("humidity");
                    String visibility = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("visibility");
                    String wind_speed = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("wind_speed");
                    String wind_deg = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("wind_deg");
                    String weather = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("weather");
                    String description = WeatherInfo.weatherList.get(WeatherInfo.weatherList.size()-1).get("description");

                    Double tempI = Double.parseDouble(temp);
                    Double feels_likeI = Double.parseDouble(feels_like);

                    msg = "temp: " + tempI + "\n" +
                           "feels_like: "+ feels_likeI+ "\n" +
                           "pressure: " + pressure + "\n" +
                           "humidity: "+ humidity + "\n" +
                           "visibility: "+ visibility + "\n" +
                           "wind_speed: " + wind_speed + "\n" +
                           "wind_deg: " + wind_deg + "\n" +
                           "weather: " + weather + "\n" +
                           "description: " + description;
                    sendEmail();
                }
            }
        });

    }

    private void sendEmail() {
        String[] TO = {email.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather Information");
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
        WeatherInfo.weatherList.remove(WeatherInfo.weatherList.size()-1);
    }

    private boolean eamilCheck(){
        String emailInput = email.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            email.setError("Please enter a valid email address");
            return false;
        }
        email.setError(null);
        return true;
    }

    private void fetchLastLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showPermissionAlert();
                return;
            }
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            emaillat = location.getLatitude();
                            emaillon = location.getLongitude();
                            Log.e("LAST LOCATION: ",String.valueOf(emaillat));
                            Log.e("LAST LOCATION: ",String.valueOf(emaillon));
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission was denied, show alert to explain permission
                    showPermissionAlert();
                }else{
                    //permission is granted now start a background service
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fetchLastLocation();
                    }
                }
            }
        }
    }

    private void showPermissionAlert(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }
}