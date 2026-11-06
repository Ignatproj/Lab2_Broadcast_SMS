package com.example.lab2broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Приёмник вещания: по клиентскому сообщению запускает SmsService.
 */
public class MyReceiver extends BroadcastReceiver {

    public static final String ACTION_SEND_SMS =
            "com.example.lab2broadcast.ACTION_SEND_SMS";
    public static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        Log.d(TAG, "onReceive: " + intent.getAction());
        Toast.makeText(context, "Receiver получил сообщение", Toast.LENGTH_SHORT).show();

        if (ACTION_SEND_SMS.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, SmsService.class);
            serviceIntent.putExtra(
                    SmsService.EXTRA_PHONE,
                    intent.getStringExtra(SmsService.EXTRA_PHONE));
            serviceIntent.putExtra(
                    SmsService.EXTRA_MESSAGE,
                    intent.getStringExtra(SmsService.EXTRA_MESSAGE));
            context.startService(serviceIntent);
            Log.d(TAG, "SmsService started from BroadcastReceiver");
        }
    }
}
