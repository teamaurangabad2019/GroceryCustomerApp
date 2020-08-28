package com.teammandroid.dairyapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.teammandroid.dairyapplication.R;



public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        bindViews();
        btnListeners();
    }

    private void bindViews() {
        back_about = findViewById(R.id.back_about);
    }
    private void btnListeners() {
        back_about.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_about:
                onBackPressed();
                break;
        }
    }
}
