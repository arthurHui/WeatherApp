package hk.edu.ouhk.comp313f_groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ViewActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.view);

        Intent intent = getIntent();
        String urlStr = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
        jsonHandlerThread.start();
        jsonHandlerThread.setURL(urlStr);
        try {
            jsonHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                WeatherInfo.weatherList,
                R.layout.view_layout,
                new String[]{WeatherInfo.TIMEZONE, WeatherInfo.TIMEZONE_OFFSET},
                new int[]{R.id.timezone, R.id.timezone_offset}
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String temp = WeatherInfo.weatherList.get(position).get("temp");
                        String feels_like = WeatherInfo.weatherList.get(position).get("feels_like");
                        String pressure = WeatherInfo.weatherList.get(position).get("pressure");
                        String humidity = WeatherInfo.weatherList.get(position).get("humidity");
                        String visibility = WeatherInfo.weatherList.get(position).get("visibility");
                        String wind_speed = WeatherInfo.weatherList.get(position).get("wind_speed");
                        String wind_deg = WeatherInfo.weatherList.get(position).get("wind_deg");
                        String weather = WeatherInfo.weatherList.get(position).get("weather");
                        String description = WeatherInfo.weatherList.get(position).get("description");
                        String icon = WeatherInfo.weatherList.get(position).get("icon");
                        String rain = WeatherInfo.weatherList.get(position).get("rain");

                        Intent intent = new Intent(ViewActivity.this, InfoActivity.class);
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

            }
        });

    }
}