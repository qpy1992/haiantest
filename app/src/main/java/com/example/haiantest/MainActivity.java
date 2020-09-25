package com.example.haiantest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_shangban;
    private Button btn_kaiji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setListeners();
    }

    protected void setViews(){
        btn_shangban = findViewById(R.id.bt_shangban);
        btn_kaiji = findViewById(R.id.bt_kaiji);
    }

    protected void setListeners(){
        btn_shangban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GetFacePhotoActivity.class));
            }
        });

        btn_kaiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}