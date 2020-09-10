package com.teammandroid.dairyapplication.admin.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.teammandroid.dairyapplication.R;
import com.teammandroid.dairyapplication.activities.AuthenticationActivity;
import com.teammandroid.dairyapplication.utils.PrefManager;
import com.teammandroid.dairyapplication.utils.Utility;

public class SelectRoleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SelectRoleActivity.class.getSimpleName();


    private CardView cvPrincipal;
    private CardView cvTeacher;
    private CardView cvParents, cv_Trust,cv_guest;
    PrefManager prefManager;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        initView();
        bundle =new Bundle();
        Log.e(TAG," "+prefManager.getROLE_ID());
        prefManager.setAUTH_BACK(1);
    }


    private void initView() {
        cvPrincipal = (CardView) findViewById(R.id.cv_Principal);
        cvPrincipal.setOnClickListener(this);
        cvTeacher = (CardView) findViewById(R.id.cv_teacher);
        cvTeacher.setOnClickListener(this);
        cvParents = (CardView) findViewById(R.id.cv_parents);
        cv_Trust = (CardView) findViewById(R.id.cv_Trust);
        cvParents.setOnClickListener(this);
        cv_guest = (CardView) findViewById(R.id.cv_guest);
        cv_guest.setOnClickListener(this);
        cv_Trust.setOnClickListener(this);
        prefManager=new PrefManager(SelectRoleActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.cv_Trust:
                //prefManager.setROLE_ID(1);
                //Toast.makeText(this, "Coming Soon..", Toast.LENGTH_SHORT).show();
                bundle.putInt("roleId",1);
                Utility.launchActivity(SelectRoleActivity.this, AuthenticationActivity.class, true,bundle);
                break;

            case R.id.cv_Principal:
                //prefManager.setROLE_ID(2);
                bundle.putInt("roleId",2);
                Utility.launchActivity(SelectRoleActivity.this, AuthenticationActivity.class, true,bundle);
                break;

            case R.id.cv_teacher:
                //prefManager.setROLE_ID(3);
                bundle.putInt("roleId",3);
                Utility.launchActivity(SelectRoleActivity.this, AuthenticationActivity.class, true,bundle);
                break;

            case R.id.cv_parents:
                prefManager.setROLE_ID(4);
                Utility.launchActivity(SelectRoleActivity.this, AuthenticationActivity.class, true);
                break;

            case R.id.cv_guest:
                prefManager.setROLE_ID(5);
                Utility.launchActivity(SelectRoleActivity.this, AuthenticationActivity.class, true);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        finish();
        // Utility.launchActivity(SelectRoleActivity.this, SplashActivity.class, true);
    }
}
