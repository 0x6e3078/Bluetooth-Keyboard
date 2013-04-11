package com.example.bluetoothkeyboard;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.view.Menu;

// Client (sender) code
public class DeviceSelector extends Activity {


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
					main.addCatagory(Intent.CATEGORY_HOME);
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
		
		Set<BluetoothDevice> paired_devices = bluetoothAdapter.getBondedDevices();
		
		if(paired_devices.size() > 0)
		{
			final StableBluetoothArrayAdapter list_adapter = new StableBluetoothArrayAdapter(this, android.R.layout.simple_list_item_1, list);
			final ListView list_view = (ListView) findViewById(R.id.list);
			listview.setAdapter(list_adapter);
			listview.setOnItemClicker
		}
		else
		{
			AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			dlg.setTitle("No Paired Devices Found");
			dlg.setMessage("Please pair with the device you wish to control before running this application.");
			dlg.setPositiveButton("Ok", new DialogInterface.OnClickListner() {
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

private class BluetoothDeviceArrayAdapter extends ArrayAdapter<String>
{
	HashMap<String, Integer> m_id_map = new HashMap<String,Integer>();
	public BluetoothArrayAdapter(Context in_context, int in_text_id, Set<BluetoothDevice> in_bt_objects)
	{
		List<String> display_names = new List<String>();

		for ( BluetoothDevice btd : in_bt_objects )
		{
			display_names.add(btd.getName());
		}

		super(context, int_text_id, display_names);

		for(int i = 0; i < in_bt_objects.size(); ++i)
		{
			m_id_map.put(in_bt_objects.get(i).getName(), i);
		}

	}

	@Override
	public long getItemId(int position) 
	{
		String item = getItem(position);
		return m_id_map.get(item);
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}
}


