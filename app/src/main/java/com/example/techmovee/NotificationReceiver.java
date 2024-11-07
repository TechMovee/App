package com.example.techmovee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Handle the notification click here
        Toast.makeText(context, "Recebado", Toast.LENGTH_LONG).show();
    }
}
