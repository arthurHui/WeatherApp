package hk.edu.ouhk.comp313f_groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class InfoActivity extends AppCompatActivity {
    private TextView tempView;
    private TextView feels_likeView;
    private TextView weatherView;
    private TextView descriptionView;
    private TextView pressureView;
    private TextView visibilityView;
    private TextView humidityView;
    private TextView wind_speedView;
    private TextView wind_degreesView;
    private ImageView imageView;
    private ImageButton warning_image;
    private double tempI;
    private double feels_likeI;
    private AlertDialog.Builder dialog;
    private double rain_hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tempView = findViewById(R.id.tempView);
        feels_likeView = findViewById(R.id.feels_likeView);
        weatherView = findViewById(R.id.weatherView);
        descriptionView = findViewById(R.id.descriptionView);
        pressureView = findViewById(R.id.pressureView);
        visibilityView = findViewById(R.id.visibilityView);
        humidityView = findViewById(R.id.humidityView);
        wind_speedView = findViewById(R.id.wind_speedView);
        wind_degreesView = findViewById(R.id.wind_degreesView);
        imageView = findViewById(R.id.imageView);
        warning_image =  (ImageButton)findViewById(R.id.warningbutton);

        Intent intent = getIntent();
        String temp = intent.getStringExtra("TEMP");
        String feels_like = intent.getStringExtra("FEELS_LIKE");
        String pressure = intent.getStringExtra("PRESSURE");
        String humidity = intent.getStringExtra("HUMIDITY");
        String visibility = intent.getStringExtra("VISIBILITY");
        String wind_speed = intent.getStringExtra("WIND_SPEED");
        String wind_deg = intent.getStringExtra("WIND_DEG");
        String weather = intent.getStringExtra("WEATHER");
        String description = intent.getStringExtra("DESCRIPTION");
        String icon = intent.getStringExtra("ICON");
        String rain = intent.getStringExtra("RAIN");

        tempI = Double.parseDouble(temp);
        feels_likeI = Double.parseDouble(feels_like);

        dialog = new AlertDialog.Builder(InfoActivity.this);

        rain_hour = Double.parseDouble(rain);

        double visibilityd = 0;
        if (Integer.parseInt(visibility) >= 1000 ){
            visibilityd = Integer.parseInt(visibility) / 1000;
        }

        tempView.setText(String.format("%.2f", tempI) + "°C");
        feels_likeView.setText(String.format("%.2f", feels_likeI) + "°C");
        weatherView.setText(weather);
        descriptionView.setText(description);
        pressureView.setText(pressure + " hPa");
        visibilityView.setText(String.format("%.2f", visibilityd) + " metres");
        humidityView.setText(humidity + "%");
        wind_speedView.setText(wind_speed + " metre/sec");
        wind_degreesView.setText(wind_deg + "° (meteorological)");

        warning_image.setVisibility(View.GONE);;
        warning_image.setOnClickListener(warning);

        switch (icon){
            case "01d":
                imageView.setImageResource(R.drawable.clear_sky);
                break;
            case "01n":
                imageView.setImageResource(R.drawable.clear_sky_n);
                break;
            case "02d":
                imageView.setImageResource(R.drawable.few_clouds);
                break;
            case "02n":
                imageView.setImageResource(R.drawable.few_clouds_n);
                break;
            case "03d":
            case "03n":
                imageView.setImageResource(R.drawable.scattered_clouds);
                break;
            case "04d":
            case "04n":
                imageView.setImageResource(R.drawable.broken_clouds);
                break;
            case "09d":
            case "09n":
                imageView.setImageResource(R.drawable.shower_rain);
                break;
            case "10d":
                imageView.setImageResource(R.drawable.rain);
                break;
            case "10n":
                imageView.setImageResource(R.drawable.rain_n);
                break;
            case "11d":
            case "11n":
                imageView.setImageResource(R.drawable.thunderstorm);
                break;
            case "13d":
            case "13n":
                imageView.setImageResource(R.drawable.snow);
                break;
            case "50d":
            case "50n":
                imageView.setImageResource(R.drawable.mist);
                break;
        }

        if(tempI > 33 || rain_hour > 30.0 || feels_likeI >33) {
            warning_image.setVisibility(View.VISIBLE);
        }

    }

    private View.OnClickListener warning = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(tempI > 33 || rain_hour > 30.0 || feels_likeI>33){
                dialog.setTitle("Special Weather Tips");
                dialog.setMessage("Hot Weather might cause adverse health effects.\n" +
                                  "The public should stay on the alert and drink more water");
                dialog.show();
            }else if(rain_hour > 30.0){
                dialog.setTitle("Special Weather Tips:AMBER rainstorm signal");
                dialog.setMessage("Heavy rain has fallen or is expected to fall generally over Hong Kong, exceeding 30 millimetres in an hour, and is likely to continue.");
                dialog.show();
            }else if(rain_hour > 50.0){
                dialog.setTitle("Special Weather Tips:RED rainstorm signal");
                dialog.setMessage("Heavy rain has fallen or is expected to fall generally over Hong Kong, exceeding 50 millimetres in an hour, and is likely to continue.");
                dialog.show();
            }else if(rain_hour > 70.0){
                dialog.setTitle("Special Weather Tips:BLACK rainstorm signal");
                dialog.setMessage("Heavy rain has fallen or is expected to fall generally over Hong Kong, exceeding 50 millimetres in an hour, and is likely to continue.");
                dialog.show();
            }
        }
    };




}