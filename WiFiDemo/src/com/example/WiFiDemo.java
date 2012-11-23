package com.example;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
//import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiDemo extends Activity implements OnClickListener {
	private static final String TAG = "WiFiDemo";
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView textStatus;
	Button buttonScan;
	Button buttonStop;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		buttonScan = (Button) findViewById(R.id.buttonScan); //scan button will get printed output when clicking this
		buttonStop = (Button) findViewById(R.id.buttonStop);
		buttonScan.setOnClickListener(this);
		buttonStop.setOnClickListener(this);
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
		
		Log.d(TAG, "onCreate()");
	}

	@Override
	public void onStop() {
		Toast.makeText(this, "stopping wifi scan",
				Toast.LENGTH_LONG).show();
		unregisterReceiver(receiver);
		//this.finish();
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	   unregisterReceiver(receiver);
	}

	public void onBackPressed() {
		Toast.makeText(this, "back pressed",
				Toast.LENGTH_LONG).show();
	//	unregisterReceiver(receiver);
	//	wifi.setWifiEnabled(false);
	      //this.finish();
	      return;
	   }


	public void onClick(View view) {
	//	Toast.makeText(this, "On Click Clicked. Toast to that!!!",
		//		Toast.LENGTH_LONG).show();

		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();
			Toast.makeText(this, "starting wifi scan",
					Toast.LENGTH_LONG).show();
			// Setup WiFi
			wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE); //start of wifi service
			wifi.setWifiEnabled(true); //enable the wifi in any case
			
			
			// Register Broadcast Receiver
			if (receiver == null)
				receiver = new WiFiScanReceiver(this);

			registerReceiver(receiver, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			textStatus.setText("");
			// List available networks  //This is what i changed from getconfiglist to getscanresult
				try{
					List<ScanResult> configs = wifi.getScanResults();
					for (ScanResult config : configs) {                 //config is of type scanresult and has all variables
						textStatus.append("\nssid : " + config.SSID + "\nlevel : " + config.level + "\n");
					}
				}
				catch (NullPointerException e){
				}
			}
		if (view.getId() == R.id.buttonStop) {
			Log.d(TAG, "onClick() wifi.endScan()");
			Toast.makeText(this, "stopping wifi scan",
					Toast.LENGTH_LONG).show();
			unregisterReceiver(receiver);
		//	WifiManager wifi1 = (WifiManager) getSystemService(Context.WIFI_SERVICE); //start of wifi service
			wifi.setWifiEnabled(false);
			System.exit(0);
		//	this.finish();
		}
	}
}
		

