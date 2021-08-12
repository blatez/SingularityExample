package com.blate.singularityexample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blate.singularity.ui.decoration.SimpleSpaceDecoration;
import com.blate.singularityexample.adapter.NumberAdapter;
import com.blate.singularityexample.databinding.ActivityRecyclerViewItemDecorationTestBinding;

public class RecyclerViewItemDecorationTestActivity
        extends AppCompatActivity {

    private ActivityRecyclerViewItemDecorationTestBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRecyclerViewItemDecorationTestBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initialize();
    }

    private void initialize() {
        mBinding.rv.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.rv.addItemDecoration(new SimpleSpaceDecoration(64,32));
        mBinding.rv.setAdapter(new NumberAdapter(103));
    }

}
