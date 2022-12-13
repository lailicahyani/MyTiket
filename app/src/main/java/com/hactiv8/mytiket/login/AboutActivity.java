package com.hactiv8.mytiket.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hactiv8.mytiket.R;

public class AboutActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
}

    public void whatsapp(View view) {
        int viewID = view.getId();
        String number = null;
        switch (viewID){
            case R.id.laili:
                number = "628980441275";
                break;
            case R.id.kaito:
                number = "6281238235204";
                break;
            default:
                break;
        }
        String url = "https://api.whatsapp.com/send?phone="+number;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}