package com.lurzapps.joinzoom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {
    private final Handler handler = new Handler();
    private final MainActivity mainActivity;

    //start the server
    public Server(MainActivity mainActivity) throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);

        this.mainActivity = mainActivity;
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public Response serve(IHTTPSession session) {
        // default html tags
        String msg = "<html><body>\n";
        Map<String, String> parms = session.getParms();
        // if link has not been entered yet, request
        if (parms.get("zl") == null) {
            msg += "<form action='?' method='get'>\n  <p>zoom-link: <input type='text' name='zl'></p>\n" + "</form>\n";
        } else {
            // else reload site with link (telling that is joining)
            final String zl = parms.get("zl");

            msg += "joining <a href='" + zl +  "'>meeting</a>...";

            // in other thread, 100ms delayed
            handler.postDelayed(() -> {
                // stop the server
                stop();

                // start the zoom meeting from link
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(zl));
                if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
                    mainActivity.startActivity(intent);
                }

                // and finish the whole app
                mainActivity.finish();
            }, 100);
        }


        return newFixedLengthResponse(msg + "</body></html>\n");
    }
}
