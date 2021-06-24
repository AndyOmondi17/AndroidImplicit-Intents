package com.andy.implicitintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    private EditText mEditTextNumber;
    private EditText mWebsiteEditText;
    private EditText mLocationEditText;
    private EditText mShareTextEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebsiteEditText = (EditText)findViewById(R.id.website_edittext);
        mLocationEditText = (EditText)findViewById(R.id.location_edittext);
        mShareTextEditText =(EditText)findViewById(R.id.share_edittext);
        mEditTextNumber = (EditText)findViewById(R.id.edit_text_phone);
    }

    public void openWebsite(View view) {
        String url = mWebsiteEditText.getText().toString();
//        Encode and parse that sting into a Uri object
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
// Find an activity to hand the intent and start that activity
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivity(intent);
        } else{
            Log.d("ImplicitIntents" ,"Can't handle this intent!");
        }
    }
    public void openLocation(View view) {
        String loc = mLocationEditText.getText().toString();
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW,addressUri);
        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        } else{
            Log.d("ImplicitIntents" ,"Can't handle this intent!");
        }
    }
    public void shareText(View view) {
        String txt = mShareTextEditText.getText().toString();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(R.string.edittext_share)
                .setText(txt)
                .startChooser();

    }
    public void makePhoneCall(){
        String number = mEditTextNumber.getText().toString();
        if(number.trim().length() > 0){
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
            }

        }else{
            Toast.makeText(MainActivity.this,"Enter Phone Number",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
       if(requestCode == REQUEST_CALL){
           if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               makePhoneCall();
           }else{
               Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void call(View view) {
        makePhoneCall();
    }
}