package com.example.sms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText PHONE_NUMBER, messed2;
    Button send, select;
    static final int PICK_CONTACT = 1;
    String[] permission = {Manifest.permission.SEND_SMS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PHONE_NUMBER = findViewById(R.id.ed1);
        messed2 = findViewById(R.id.messageed1);
        select = findViewById(R.id.buttonselect);
        send = findViewById(R.id.bt1);

        ActivityCompat.requestPermissions(MainActivity.this, permission, 0);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = PHONE_NUMBER.getText().toString();
                String mess = messed2.getText().toString();

                if (haspermisiion(getApplicationContext(), permission[0])) {
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phone_no, null, mess, null, null);
                    PHONE_NUMBER.setText("");
                    messed2.setText("");
                    Toast.makeText(getApplicationContext(), "message send successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "message not send successfully", Toast.LENGTH_LONG).show();


                }
            }
        });

    }

    public boolean haspermisiion(Context context, String Permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context != null && Permissions != null) {
            if (ActivityCompat.checkSelfPermission(context, Permissions) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }


        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                "DISPLAY_NAME = '"  + "'", null, null);
        if (cursor.moveToFirst()) {
            String contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //
            //  Get all phone numbers.
            //
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                switch (type) {
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        // do something with the Home number here...
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        // do something with the Mobile number here...
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        // do something with the Work number here...
                        break;
                }
            }
            phones.close();
        }
        cursor.close();
    }
}



