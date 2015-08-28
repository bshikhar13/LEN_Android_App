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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    public void senddata(String ssid0, String rssi0){
            String data = "";
            try{
                data = URLEncoder.encode("ssid0", "UTF-8") + "=" + URLEncoder.encode(ssid0, "UTF-8");

                data += "&" + URLEncoder.encode("rssi0", "UTF-8") + "=" + URLEncoder.encode(rssi0, "UTF-8");

            }catch (Exception e){

            }
        Log.v(TAG, "data = " + data);
            String text = "";
            BufferedReader reader=null;
            try{
                URL url = new URL("http://192.168.0.3/LEN/LENdata.php");
                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

            }catch (Exception e){
                Log.v(TAG, "status  = " + e);
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
        final int delay = 1000; //milliseconds

        h.postDelayed(new Runnable(){
            public void run(){
                itemsAdapter.clear();
                ScanResult result0 = wifi.getScanResults().get(0);
                String ssid0 = result0.SSID;
                int rssi0 = result0.level;
                double distance  = calculatedistance(rssi0);
                String rssiString0 = String.valueOf(rssi0);
                String distance0 = String.valueOf(distance);
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
