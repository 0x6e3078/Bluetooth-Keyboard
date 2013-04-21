package com.example.bluetoothkeyboard;

// import android.content.Intent;
import java.util.UUID; import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.view.Menu;
import android.bluetooth.*;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Keyboard extends Activity {
    private BluetoothSocket m_bt_socket;
    private BluetoothDevice m_device;
    private BluetoothAdapter m_bt;
    private BluetoothThread m_thread;

    //TODO: On KeyUp/OnKeyDown methods. MAKE SURE TO IGNORE KEYS UNTIL
    //m_bt_socket.isConnected() is true, in case of hardware keyboard.
    //Encapsulate with KeyPacket.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keyboard);
        String dev_name = getIntent().getStringExtra("DeviceName"); 
        String dev_addr = getIntent().getStringExtra("DeviceAddress");
        String dev_bt = getIntent().getStringExtra("BluetoothAdapter");
        dev_bt.trim();
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
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
        //Android only supports one bluetooth device, this is for
        //futureproofing.
         m_bt = BluetoothAdapter.getDefaultAdapter();


        for( BluetoothDevice btd : m_bt.getBondedDevices())
        {
            if(btd.getName() == dev_name && btd.getAddress() == dev_addr)
            {
                m_device = btd;
                break;
            }
        }



        try
        {
             m_bt_socket = m_device.createRfcommSocketToServiceRecord(UUID.fromString("deadbeef-dead-beef-dead-beefdeadbeef"));
        }
        catch (IOException e)
        {
			dlg.setTitle("There was a problem connecting.");
			dlg.setMessage("The problem was with your device.");
			dlg.show();
            
        }
        m_bt.cancelDiscovery();


        //TODO: Prompt user somehow, that we're doing something.
        try
        {
            m_bt_socket.connect();
        }
        catch (IOException connectException)
        {
            try
            {
                m_bt_socket.close();
            }
            catch(IOException closeException)
            {
                dlg.setTitle("Socket could not close");
                dlg.setMessage("Sorry threw an IOException.");
                dlg.show();

            }
        }

        try
        {
            m_thread = new BluetoothThread(m_bt_socket);
        }
        catch(IOException e)
        {
            dlg.setTitle("Could not open streams.");
            dlg.setMessage("Socket ERror.");
            dlg.show();

        }
        //TODO: Connected bring up the keyboard, allow for keypresses to go
        //through.
        
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.keyboard, menu);
		return true;
	}

    private class BluetoothThread extends Thread
    {
        private final BluetoothSocket m_bt_socket;
        private final InputStream m_in_stream;
        private final OutputStream m_out_stream;

        public BluetoothThread(BluetoothSocket in_socket) throws IOException
        {
            m_bt_socket = in_socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try 
            {
                tmpIn = in_socket.getInputStream();
                tmpOut = in_socket.getOutputStream();
            }
            catch (IOException e)
            {
                throw new IOException("Stream Error");
            }

            m_in_stream = tmpIn;
            m_out_stream = tmpOut;

                        
        }

        public void run()
        {
             byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = m_in_stream.read(buffer);
                    // Send the obtained bytes to the UI activity
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) throws IOException
        {
            try 
            {
                m_out_stream.write(bytes);
            } catch (IOException e)
            { 
                throw e;
            }
        }

    /* Call this from the main activity to shutdown the connection */
        public void cancel() throws IOException
        {
            try 
            {
                m_bt_socket.close();
            } 
            catch (IOException e) 
            { 
                throw e;
            }
        }
    };
}

