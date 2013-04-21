package com.example.bluetoothkeyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// Client (sender) code


public class DeviceSelector extends Activity {
	private final static int REQUEST_ENABLE_BT = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_selector);
		
		//Check if we have Bluetooth
		
		BluetoothAdapter bt_adapter = BluetoothAdapter.getDefaultAdapter();
		if (bt_adapter == null)
		{
			AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			dlg.setTitle("No Bluetooth Devices Found");
			dlg.setMessage("This application needs at least one Bluetooth device to function properly");
			dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener () {
				//Exit on OK
				public void onClick(DialogInterface dialog, int which)
				{
					Intent main = new Intent(Intent.ACTION_MAIN);
					main.addCategory(Intent.CATEGORY_HOME);
					main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(main);
				}
			});
			dlg.show();
			
		}

		//Check if Bluetooth is on
		if (!bt_adapter.isEnabled())
		{
			//Start
			Intent enable_bt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enable_bt, REQUEST_ENABLE_BT);
		}
		
		Set<BluetoothDevice> paired_devices = bt_adapter.getBondedDevices();
		
		if(paired_devices.size() > 0)
		{
			final BluetoothDeviceArrayAdapter list_adapter = new BluetoothDeviceArrayAdapter(this, android.R.layout.simple_list_item_1, paired_devices);
			final ListView list_view = (ListView) findViewById(R.id.list);
			list_view.setAdapter(list_adapter);
			//TODO: Set up item selection to go to next activity. MAKE SURE TO
            //READ Keyboard.java TO SEE WHAT EXTRAS ARE NEEDED IN INTENT.
		}
		else
		{
			AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			dlg.setTitle("No Paired Devices Found");
			dlg.setMessage("Please pair with the device you wish to control before running this application.");
			dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				//Launch Bluetooth Settings
				public void onClick(DialogInterface dialog,int which)
				{
					Intent bt_settings = new Intent(Intent.ACTION_MAIN);
					bt_settings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
					startActivity(bt_settings);
				}
			});
		}

	
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_device_selector, menu);
		return true;
	}

}

class BluetoothDeviceArrayAdapter extends ArrayAdapter<String>
{
	HashMap<String, Integer> m_id_map = new HashMap<String,Integer>();
	public BluetoothDeviceArrayAdapter(Context in_context, int in_text_id, Set<BluetoothDevice> in_bt_objects)
	{
		super(in_context, in_text_id, get_names(in_bt_objects));
		BluetoothDevice[] bt_obj_array = in_bt_objects.toArray(new BluetoothDevice[0]);

		for(int i = 0; i < in_bt_objects.size(); ++i)
		{
			m_id_map.put(bt_obj_array[i].getName(), i);
		}

	}
		@Override
	public long getItemId(int position) 
	{
		String item = getItem(position);
		return m_id_map.get(item);
	}
	private static List<String> get_names(Set<BluetoothDevice> in_bt_obj)
	{
		List<String> names = new ArrayList<String>();
		for ( BluetoothDevice btd : in_bt_obj )
		{
			names.add(btd.getName());
		}
		
		return names;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}
}


