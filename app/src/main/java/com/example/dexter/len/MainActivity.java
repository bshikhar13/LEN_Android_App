package com.example.dexter.len;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.*;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SBActivity";
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    public double calculatedistance(int rssi){
        int A = -35;
        double n = 2.2;
        double distance = Math.pow(10.0, ((A - rssi) / (10 * n)));
        return distance;
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public void senddata(String ssid0, String rssi0){

            try{
                ssid0 = URLEncoder.encode(ssid0,"UTF-8");
                rssi0 = URLEncoder.encode(rssi0,"UTF-8");

                String url_request = "http://192.168.1.10/LEN/LENdata.php?ssid0="+ssid0+"&rssi0="+rssi0;
                URL url = new URL(url_request);

                new CallAPI().execute(url_request);

            }catch (Exception e){
                Log.i("SBS", e.toString());
            }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems=(ListView)findViewById(R.id.listView);

        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        final Handler h = new Handler();
        final int delay = 20; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                itemsAdapter.clear();
                ScanResult result0 = wifi.getScanResults().get(0);
                String ssid0 = result0.SSID;
                int rssi0 = result0.level;

                String rssiString0 = String.valueOf(rssi0);

                items.add("\n" + ssid0 + "   " + rssiString0 + "   : " + distance0);
                senddata(ssid0,rssiString0);
                try {
                    ScanResult result1 = wifi.getScanResults().get(1);
                    String ssid1 = result1.SSID;
                    int rssi1 = result1.level;
                    String rssiString1 = String.valueOf(rssi1);
                    items.add("\n" + ssid1 + "   " + rssiString1);
                }catch (Exception e2){

                }


                try {
                    ScanResult result2 = wifi.getScanResults().get(2);
                    String ssid2 = result2.SSID;
                    int rssi2 = result2.level;
                    String rssiString2 = String.valueOf(rssi2);
                    items.add("\n" + ssid2 + "   " + rssiString2);
                }catch (Exception e3){

                }


                try {
                    ScanResult result3 = wifi.getScanResults().get(3);
                    String ssid3 = result3.SSID;
                    int rssi3 = result3.level;
                    String rssiString3 = String.valueOf(rssi3);
                    items.add("\n" + ssid3 + "   " + rssiString3);
                }catch (Exception e){

                }


                h.postDelayed(this, delay);
            }
        }, delay);



    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String urlString=params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;

            // HTTP Get
            try {

                URL url = new URL(urlString);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                in = new BufferedInputStream(urlConnection.getInputStream());

            } catch (Exception e ) {

                System.out.println(e.getMessage());

                return e.getMessage();

            }

            return resultToDisplay;

        }

        protected void onPostExecute(String result) {

        }

    } // end CallAPI

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
