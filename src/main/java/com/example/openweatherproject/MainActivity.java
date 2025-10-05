package com.example.openweatherproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    StringBuilder jsonString;
    StringBuilder jsonString2;
    ListView listView;

    String response;
    public int image;
    String name;
    String lat;
    String lon;
    EditText editText;
    String zipCode;
    TextView weatherTextView;
    TextView latTextView;
    TextView lonTextView;
    TextView locTextView;
    ImageView imageView2;

    int cImage;
    TextView quoteTextView;
    String weatherDescription;
    String data2;

    ArrayList<String> minTempValues = new ArrayList<>();
    ArrayList<String>  maxTempValues = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> dates = new ArrayList<>();

    ArrayList<WeatherData> wList = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    JSONObject weather;

    String zC;
    Button button;
    CustomAdapter adapter;

    public void setAdapter()
    {
        adapter = new CustomAdapter(this, R.layout.adapter_layout, wList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.zipCodeEditText);
        button = findViewById(R.id.submitBtn);
        weatherTextView = findViewById((R.id.weatherTextView));
        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        locTextView = findViewById(R.id.locTextView);
        listView = findViewById(R.id.listView);
        quoteTextView = findViewById(R.id.quoteTextView);
        imageView2 = findViewById(R.id.imageView2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCode = editText.getText().toString();

                try {
                    if (!zipCode.isEmpty()) {
                        //ArrayList<WeatherData> wList2 = new ArrayList<>();
                        //wList2 = wList;
                        //wList2.clear();
                        wList.clear();
                        descriptions.clear();
                        minTempValues.clear();
                        maxTempValues.clear();
                        dates.clear();
                        images.clear();
                        new AsyncThread(zipCode).execute();
                        setAdapter();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter a ZIP code", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid ZIP code", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class AsyncThread extends AsyncTask<String, Void, Void> {
        private String zip;

        public AsyncThread(String zipCode) {
            this.zip = zipCode;
        }

        @Override
        protected Void doInBackground(String... strings) {
//            String zip = strings[0];
            String inputLine = null;
            String inputLine2 = null;
            try {
                URL url = new URL("https://api.openweathermap.org/geo/1.0/zip?zip="+zip+",US&appid=ba86f83a7f6d97d8134a5ba145aab073");
                URLConnection urlcon = url.openConnection();
                InputStream stream = urlcon.getInputStream();
                BufferedReader bR = new BufferedReader(new InputStreamReader(stream));
                jsonString = new StringBuilder();
                while ((inputLine = bR.readLine()) != null) {
                    jsonString.append(inputLine);
                }
                bR.close();
                String data = jsonString.toString();
                JSONObject jsonObject = new JSONObject(data);
                String jsonString = jsonObject.toString();

                Log.d("name", ""+ jsonObject.getString("name"));
                Log.d("zip", ""+ jsonObject.getString("zip"));
                Log.d("zip", ""+ jsonObject.getString("zip"));
                Log.d("lat", ""+ jsonObject.getString("lat"));
                Log.d("lon", ""+ jsonObject.getString("lon"));
                Log.d("country", ""+ jsonObject.getString("country"));

                name = jsonObject.getString("name");
                lat = jsonObject.getString("lat");
                lon = jsonObject.getString("lon");

                URL url2 = new URL("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=ba86f83a7f6d97d8134a5ba145aab073&units=imperial");
                URLConnection urlcon2 = url2.openConnection();
                InputStream stream2 = urlcon2.getInputStream();
                BufferedReader bR2 = new BufferedReader(new InputStreamReader(stream2));
                jsonString2 = new StringBuilder();
                while ((inputLine2 = bR2.readLine()) != null) {
                    jsonString2.append(inputLine2);
                }
                bR2.close();
                data2 = jsonString2.toString();
                JSONObject jsonObject2 = new JSONObject(data2);
                String jS2 = jsonObject2.toString();
                Log.d("JSON", jS2);




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                jsonString2 = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // This is where you will download your data.
            // You will need to override another method to update the UI
            Log.d("TAG", "Thread ");
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.d("TAG", "onPostExecute");
            Log.d("JSON", String.valueOf(jsonString2));
            //weatherTextView.setText(String.valueOf(jsonString2));

            if (jsonString2 == null) {
                Toast.makeText(MainActivity.this, "Please enter a valid ZIP code", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse the JSON response
            JSONObject jsonObj = null;
            // Get the weather array
            JSONArray weatherArray = null;
            try {
                jsonObj = new JSONObject(String.valueOf(jsonString2));
                //weatherArray = jsonObject.getJSONArray("weather");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            // JSONObject jsonObject = new JSONObject(String.valueOf(jsonString2));
            JSONArray listArray = null;
            try {
                listArray = jsonObj.getJSONArray("list");


                for(int i = 0; i<listArray.length(); i+=8) {
                    Log.d("TAG", "onPostExecute - inside listArray for loop");
                    JSONObject ListItem = listArray.getJSONObject(i);
                    //JSONObject thirdListItem = listArray.getJSONObject(2);
                    Log.d("TAG", ListItem.toString());
                    JSONObject main = ListItem.getJSONObject("main");
                    JSONArray weather = ListItem.getJSONArray("weather");
                    //  JSONObject weather = weatherArray.getJSONObject(0);
                    String desc = weather.getJSONObject(0).getString("description");
                    String minTemp = main.getString("temp_min");
                    String maxTemp = main.getString("temp_max");
                    String dt_Text = ListItem.getString("dt_txt");
                    minTempValues.add(minTemp);
                    maxTempValues.add(maxTemp);
                    descriptions.add(desc);
                    dates.add(dt_Text);

                    if (desc.contains("rain"))
                    {
                        images.add(R.drawable.capamerica);
                    }
                    else if(desc.contains("sun"))
                    {
                        images.add(R.drawable.loki1);
                    }
                    else if(desc.contains("clear"))
                    {
                        images.add(R.drawable.thanos);
                    }
                    else if(desc.contains("snow"))
                    {
                        images.add(R.drawable.ancientone);
                    }
                    else if(desc.contains("cloud"))
                    {
                        images.add(R.drawable.capmarvel);
                    }
                    Log.d("TAGWEATHER", desc);
                    Log.d("minTemp", minTemp);
                    Log.d("maxTemp", maxTemp);

                    Log.d("date", dt_Text);

                    String temp = main.getString("temp");
                    Log.d("TAG", temp);
                    setAdapter();
                    //adapter.notifyDataSetChanged();
                    //Log.d("TAG", desc);
                }
                Log.d("2ndmintemp",minTempValues.get(1));
                //setAdapter();

                WeatherData day1 = new WeatherData(dates.get(0), minTempValues.get(0), maxTempValues.get(0), images.get(0), descriptions.get(0));
                WeatherData day2 = new WeatherData(dates.get(1), minTempValues.get(1), maxTempValues.get(1), images.get(1), descriptions.get(1));
                WeatherData day3 = new WeatherData(dates.get(2), minTempValues.get(2), maxTempValues.get(2), images.get(2), descriptions.get(2));
                WeatherData day4 = new WeatherData(dates.get(3), minTempValues.get(3), maxTempValues.get(3), images.get(3), descriptions.get(3));
                WeatherData day5 = new WeatherData(dates.get(4), minTempValues.get(4), maxTempValues.get(4), images.get(4), descriptions.get(4));
                wList.add(day1);
                wList.add(day2);
                wList.add(day3);
                wList.add(day4);
                wList.add(day5);



                JSONObject firstListItem = listArray.getJSONObject(0);
                JSONObject main = firstListItem.getJSONObject("main");
                String cTemp = main.getString("temp");
                JSONArray weather1 = firstListItem.getJSONArray("weather");
                String cDesc = weather1.getJSONObject(0).getString("description");
                if (cDesc.contains("rain"))
                {
                    quoteTextView.setText("Captain America: I'm gonna have to rain check that dance.");
                    imageView2.setImageResource(R.drawable.capamerica);
                }
                else if(cDesc.contains("sun"))
                {
                   quoteTextView.setText("Loki: I assure you, brother... the sun will shine on us again.");
                    imageView2.setImageResource(R.drawable.loki1);
                }
                else if(cDesc.contains("clear"))
                {
                    quoteTextView.setText("Thanos: The children born have known nothing but full bellies and clear skies. It's a paradise.");
                    imageView2.setImageResource(R.drawable.thanos);
                }
                else if(cDesc.contains("snow"))
                {
                    quoteTextView.setText("Ancient One: You'd think after all this time, I'd be ready. But look at me. Stretching one moment into a thousand... just so I can watch the snow.");
                    imageView2.setImageResource(R.drawable.ancientone);
                }
                else if(cDesc.contains("cloud"))
                {
                    quoteTextView.setText("Captain Marvel: In the clouds");
                    imageView2.setImageResource(R.drawable.capmarvel);
                }
                weatherTextView.setText("CURRENT TEMP: " + cTemp + "°");
                latTextView.setText("LATITUDE: " + lat);
                lonTextView.setText("LONGITUDE: " + lon);
                locTextView.setText("LOCATION: " + name);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            setAdapter();

        }

    }
}
