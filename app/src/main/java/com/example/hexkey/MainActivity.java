package com.example.hexkey;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onEncodeScreen(View view) {
        Intent intent = new Intent(this, Encode.class);
        startActivity(intent);
    }

    public void onDecodeScreen(View view) {
        Intent intent = new Intent(this, Decode.class);
        startActivity(intent);
    }

}
