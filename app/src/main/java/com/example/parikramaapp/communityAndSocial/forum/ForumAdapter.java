package com.example.parikramaapp.communityAndSocial.forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;

import java.util.ArrayList;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    private ArrayList<String> forumTitles;
    private OnItemClickListener mListener;

    // Interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Method to set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ForumAdapter(ArrayList<String> forumTitles) {
        this.forumTitles = forumTitles;
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum, parent, false);
        return new ForumViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        String title = forumTitles.get(position);
        holder.bind(title);
    }

    @Override
    public int getItemCount() {
        return forumTitles.size();
    }

    // ViewHolder class
    public static class ForumViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        public ForumViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);

            // Set click listener for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        // Bind data to the views
        public void bind(String title) {
            textViewTitle.setText(title);
        }
    }
}
