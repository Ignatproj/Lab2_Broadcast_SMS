package com.example.lab2broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/** Вариант 2: сервис SMS (запускается из BroadcastReceiver). */
public class SmsService extends Service {

    public static final String TAG = "SmsService";
    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_MESSAGE = "message";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "SmsService Created", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "SmsService Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String phone = "5556";
        String message = "SMS через BroadcastReceiver";

        if (intent != null) {
            if (intent.getStringExtra(EXTRA_PHONE) != null) {
                phone = intent.getStringExtra(EXTRA_PHONE);
            }
            if (intent.getStringExtra(EXTRA_MESSAGE) != null) {
                message = intent.getStringExtra(EXTRA_MESSAGE);
            }
        }

        Log.d(TAG, "Sending SMS to " + phone + ": " + message);
        Toast.makeText(this, "Отправка SMS на " + phone, Toast.LENGTH_LONG).show();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(this, "SMS отправлено", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "SMS sent successfully");
        } catch (SecurityException e) {
            Toast.makeText(this, "Нет разрешения SEND_SMS", Toast.LENGTH_LONG).show();
            Log.e(TAG, "SEND_SMS permission missing", e);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "SMS error", e);
        }

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "SmsService Destroyed");
        super.onDestroy();
    }
}
