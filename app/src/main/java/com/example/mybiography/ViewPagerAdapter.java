package com.example.mybiography;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewHolderPage> {

    private Context context = null;

    private ArrayList<Job> listData;

    ViewPagerAdapter(ArrayList<Job> data) {
        this.listData = data;
    }

    @Override
    public ViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPage holder, int position) {
        if (holder instanceof ViewHolderPage) {
            ViewHolderPage viewHolder = (ViewHolderPage) holder;
            Log.d("ViewPagerAdapter35", listData.get(position).jobName);
            viewHolder.onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
