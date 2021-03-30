package com.lurzapps.joinzoom;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create text view as main content view
        TextView textView = new TextView(this);
        setContentView(textView);

        // give explanation
        String ipAddr = Utils.getIPAddress(true);
        textView.setText(getString(R.string.open_desc, ipAddr));

        // start the server
        try {
            server = new Server(this);
        } catch (IOException e) {
            Log.e("socket failed", "", e);
            // if failed finish whole app
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(server != null)
            server.stop();
    }
}