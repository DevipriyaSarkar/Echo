package echo.echo.echo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static Main2Activity inst;
    ArrayList<String> wifiMessagesList = new ArrayList<String>();
    ListView wifiListView;
    ArrayAdapter arrayAdapter;
    List<ScanResult> scanList;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //registerReceiver(new NetworkChangeReceiver(), new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));


        wifiListView = (ListView) findViewById(R.id.wifiList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wifiMessagesList);
        wifiListView.setAdapter(arrayAdapter);
        wifiListView.setOnItemClickListener(this);

        getWifiNetworksList();

    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    private void getWifiNetworksList(){


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final WifiManager wifiManager =
                (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        registerReceiver(new BroadcastReceiver() {


            @SuppressLint("UseValueOf")
            @Override
            public void onReceive(Context context, Intent intent) {
                scanList = wifiManager.getScanResults();
                arrayAdapter.clear();
                for (int i = 0; i < scanList.size(); i++) {

                    String str =   (scanList.get(i).SSID).toString() + "\n" + (scanList.get(i).BSSID).toString();

                    arrayAdapter.add(str);
                }
            }


        }, filter);
        wifiManager.startScan();
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

        String[] wifiMessages;

        wifiMessages= wifiMessagesList.get(pos).split("\n");

            Toast.makeText(this, wifiMessages[0] + wifiMessages[0]  , Toast.LENGTH_SHORT).show();



        result = wifiMessages[1];
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
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
