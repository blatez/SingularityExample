package com.blate.singularityexample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.blate.singularityexample.R;
import com.blate.singularityexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setViewListener();
    }

    private void setViewListener() {
        mBinding.btUiTest.setOnClickListener(v -> {
            startActivity(new Intent(this, UiTestActivity.class));
        });
    }

}