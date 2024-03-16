package com.example.parikramaapp.communityAndSocial.forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;

import java.util.ArrayList;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {

    private ArrayList<String> discussionTitles;
    private ArrayList<String> discussionIds;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    DiscussionAdapter(Context context, ArrayList<String> titles, ArrayList<String> ids) {
        this.mInflater = LayoutInflater.from(context);
        this.discussionTitles = titles;
        this.discussionIds = ids;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discussion, parent, false);
        return new ViewHolder(view);
    }

    public interface ItemClickListener {
        void onItemClick(String discussionId);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = discussionTitles.get(position);
        holder.tvDiscussionTitle.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(discussionIds.get(position));
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiscussionTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDiscussionTitle = itemView.findViewById(R.id.tvDiscussionTitle);
        }
    }

    @Override
    public int getItemCount() {
        return discussionTitles.size();
    }


    String getItem(int id) {
        return discussionIds.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
