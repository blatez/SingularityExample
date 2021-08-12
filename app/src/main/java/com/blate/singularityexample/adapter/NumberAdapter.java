package com.blate.singularityexample.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blate.singularityexample.databinding.CellNumberBinding;

public class NumberAdapter
        extends RecyclerView.Adapter<NumberAdapter.NumberViewHolder> {

    private final int mCount;

    public NumberAdapter(int count) {
        mCount = count;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NumberViewHolder(CellNumberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NumberAdapter.NumberViewHolder holder, int position) {
        holder.binding.content.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class NumberViewHolder
            extends RecyclerView.ViewHolder {

        CellNumberBinding binding;

        public NumberViewHolder(@NonNull CellNumberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
