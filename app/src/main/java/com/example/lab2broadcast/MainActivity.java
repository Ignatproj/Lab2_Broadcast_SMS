package com.example.lab2broadcast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int REQ_SMS = 1001;

    Button buttonBroadcast;
    EditText editPhone, editSms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBroadcast = findViewById(R.id.buttonBroadcast);
        editPhone = findViewById(R.id.editPhone);
        editSms = findViewById(R.id.editSms);

        buttonBroadcast.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.SEND_SMS}, REQ_SMS);
        }
    }

    @Override
    public void onClick(View src) {
        if (src.getId() == R.id.buttonBroadcast) {
            broadcastIntent();
        }
    }

    /** Клиентское сообщение в BroadcastReceiver. */
    public void broadcastIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Нужно разрешение SEND_SMS", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.SEND_SMS}, REQ_SMS);
            return;
        }

        Intent intent = new Intent(MyReceiver.ACTION_SEND_SMS);
        intent.setPackage(getPackageName());
        intent.putExtra(SmsService.EXTRA_PHONE, editPhone.getText().toString().trim());
        intent.putExtra(SmsService.EXTRA_MESSAGE, editSms.getText().toString().trim());

        Log.d(TAG, "sendBroadcast → MyReceiver → SmsService");
        Toast.makeText(this, "Broadcast отправлен", Toast.LENGTH_SHORT).show();
        sendBroadcast(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_SMS) {
            boolean ok = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(this,
                    ok ? "Разрешение SMS получено" : "Без SEND_SMS SMS не отправится",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
