package com.example.parikramaapp.communityAndSocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parikramaapp.R;

public class GridItemAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mOptions;
    private int[] mIcons;

    public GridItemAdapter(@NonNull Context context, String[] options, int[] icons) {
        super(context, R.layout.grid_item_layout);
        mContext = context;
        mOptions = options;
        mIcons = icons;
    }

    @Override
    public int getCount() {
        return mOptions.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.grid_item_layout, null);
            holder = new ViewHolder();
            holder.icon = view.findViewById(R.id.grid_item_icon);
            holder.text = view.findViewById(R.id.grid_item_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.icon.setImageResource(mIcons[position]);
        holder.text.setText(mOptions[position]);

        return view;
    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
    }
}
