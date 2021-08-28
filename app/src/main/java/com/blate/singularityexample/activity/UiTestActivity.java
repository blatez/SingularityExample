package com.blate.singularityexample.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blate.singularity.repository.MediaTools;
import com.blate.singularity.ui.ViewPainter;
import com.blate.singularityexample.databinding.ActivityMainBinding;
import com.blate.singularityexample.databinding.ActivityUiTestBinding;

import java.io.File;

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
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1001);

        mBinding.btRecyclerViewItemDecorationTest.setOnClickListener(v -> {
            startActivity(new Intent(this, RecyclerViewItemDecorationTestActivity.class));
        });

        mBinding.btViewPainterTest.setOnClickListener(v -> {
            TextView textView = new TextView(this);
            textView.setTextColor(0xFFFFFFFF);
            textView.setBackgroundColor(0xFF8F4FFF);
            textView.setText("This TextView create by [new]1");

            ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));

            Bitmap bitmap = ViewPainter.saveViewSnapshot(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Uri uri = MediaTools.saveImageToAlbum(
                    this,
                    bitmap,
                    null,
                    String.format("%s.jpg", System.currentTimeMillis()));

            if (uri != null) {
                MediaTools.refreshAlbum(this, uri);
                mBinding.iv.setImageBitmap(bitmap);
                Toast.makeText(this, String.format("save to album1 [%s]", uri), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "save fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
