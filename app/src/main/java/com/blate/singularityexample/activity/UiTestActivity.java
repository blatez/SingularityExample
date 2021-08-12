package com.blate.singularityexample.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blate.singularityexample.databinding.ActivityUiTestBinding;

public class UiTestActivity
        extends AppCompatActivity {

    private ActivityUiTestBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUiTestBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setViewListener();
    }

    private void setViewListener() {
        mBinding.btRecyclerViewItemDecorationTest.setOnClickListener(v -> {
            startActivity(new Intent(this, RecyclerViewItemDecorationTestActivity.class));
        });

    }

}
