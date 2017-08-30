package com.weatherapp.weatherapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Main activity class
public class MainActivity extends AppCompatActivity {

    TextView result1;
    TextView temp;
    char tmpSymbol = '\u00B0';


    EditText cityName;

    // This method would be called when What's the weather button would be pressed
    public void findtheweather(View view) {

        //Assigning the value which user entered to String s
        String s = cityName.getText().toString();

        // Calling download task function
        download task = new download();

        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + s + "uk&appid=f7006712e3f4fb2b160669175fc7c254");
        //http://api.openweathermap.org/data/2.5/weather?q=suk&appid=f7006712e3f4fb2b160669175fc7c254
    }

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Using id to tell where to make changes
        cityName = (EditText) findViewById(R.id.cityname);


    }

    // Creating download method with Async Task ( we r going to use this to get data from internet and parse it )
    private class download extends AsyncTask<String, Void, String> {



        // do doInBackground method we r using this method to download Json from site.
        @Override
        protected String doInBackground(String... urls) {
            String result = "";

//            calling url as url
            URL url;
//            calling HttpUrlConnection as urlConnection
            HttpURLConnection urlConnection;

//            Using try and catch block to find any errors
            try {
                // assigning url value of first object in array called urls which is declared in this start of this method
                url = new URL(urls[0]);

                // using urlConnection to open url which we assigning URL before
                urlConnection = (HttpURLConnection) url.openConnection();

                // Using InputStream to download the content
                InputStream inputStream = urlConnection.getInputStream();
                // Using InputStreamReader to read the inputstream or the data we r downloading
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

//                using it to check if we reached the end of String / Data
                int Data = inputStreamReader.read();
//              using While loop to assign that data to string called result because InputStreamReader reads only one character at a time
                while (Data != -1) {

                    char current = (char) Data;

                    result += current;

                    Data = inputStreamReader.read();
                }

                // returning value of result

                return result;

//                Try and catch block to catch any errors
            } catch (Exception e) {
//                e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
                e.printStackTrace();
            }

            return null;
        }

        // This method execute after doInBackground method and Parse the Json
        @Override
        protected void onPostExecute(String result) {
// Try and catch block to catch any errors
            try {

                // linking result1 with textView with id result
                result1 = (TextView) findViewById(R.id.result);

//                calling  JSONObject as jsonObject
                JSONObject jsonObject = new JSONObject(result);

//                using jsonObject to get String from Json which is tagged as weather
                String weather = jsonObject.getString("weather");

                JSONObject main  = jsonObject.getJSONObject("main");
                String temperature = main.getString("temp");
                float tempDouble = Float.parseFloat(temperature);
                // Kelvin to Degree Celsius Conversion
                float celsius = tempDouble - 273.15F;
               // Log.d("temp", "Celsius: "+celsius);
                int tempInt = (int) Math.round(celsius);
                // Log.d("temp", "onPostExecute: "+temperature);
                temp = (TextView) findViewById(R.id.temp);

//        calling JSONArray as arr
                JSONArray arr    = new JSONArray(weather);
// using for loop to loop through arr array
                for (int i = 0; i <= arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);


                    // using result1 to set Text of that Text view with id Result  to this
                   // result1.setText(String.format("Weather : %s , Description : %s , Temp : %d", jsonPart.getString("main"), jsonPart.getString("description"), tempInt));
                      result1.setText(String.format("Weather : %s , Temp : %d", jsonPart.getString("main"), tempInt)+ " \u2103");

                   // temp.setText(tempInt);
                   // Toast.makeText(MainActivity.this,"temp"+tempInt,Toast.LENGTH_LONG).show();

                    /*String icon = jsonPart.getString("icon");
                    String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
                     Picasso.with(this).load(iconUrl).into(yourImageView);*/

                }



               // String main = jsonObject.getString("main");
                //Log.d("main", "onPostExecute: "+main);


               /* JSONObject main  = jsonObject.getJSONObject("main");
                String temperature = main.getString("temp");
                Log.d("temp", "onPostExecute: "+temperature);
                TextView temp = (TextView) findViewById(R.id.temp);
                temp.setText(temperature);*/

//                e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
            } catch (Exception e) {
                e.printStackTrace();
            }

        }




    }

}
