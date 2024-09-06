package com.example.exam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Models.TestInfo;
import com.example.exam.R;

import java.util.List;

public class TestInfoAdapter extends RecyclerView.Adapter<TestInfoAdapter.TestInfoViewHolder> {

    private final List<TestInfo> testInfoList;
    private final Activity context;

    public TestInfoAdapter(Activity context, List<TestInfo> testInfoList) {
        this.testInfoList = testInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public TestInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_item_layout_admin, parent, false); // Use your layout
        return new TestInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestInfoViewHolder holder, int position) {
        TestInfo currentTest = testInfoList.get(position);
        holder.testIdTextView.setText("Test " + currentTest.getTestId());
        holder.testTimeTextView.setText("Time: " + currentTest.getTestTime());
    }

    @Override
    public int getItemCount() {
        return testInfoList.size();
    }

    public static class TestInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView testIdTextView;
        public TextView testTimeTextView;

        public TestInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            testIdTextView = itemView.findViewById(R.id.testIdTextView11);
            testTimeTextView = itemView.findViewById(R.id.testTimeTextView11);
        }
    }
}