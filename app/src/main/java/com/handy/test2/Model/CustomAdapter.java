package com.handy.test2.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handy.test2.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int[] flags;
    String[] currencyNames;
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] flags, String[] currencyNames) {
        this.context = applicationContext;
        this.flags = flags;
        this.currencyNames = currencyNames;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = view.findViewById(R.id.imageView);
        TextView names = view.findViewById(R.id.textView);
        icon.setImageResource(flags[i]);
        names.setText(currencyNames[i]);
        return view;
    }
}
