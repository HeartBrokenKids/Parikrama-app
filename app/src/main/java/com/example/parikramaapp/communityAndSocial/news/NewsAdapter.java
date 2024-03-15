package com.example.parikramaapp.communityAndSocial.news;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        Log.d("ARTICLESEUPDATES", this.articles.toString());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_article, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (articles != null && position < articles.size()) {
            Article article = articles.get(position);
            holder.titleTextView.setText(article.getTitle());
            holder.descriptionTextView.setText(article.getDescription());
            holder.publishedAtTextView.setText(article.getPublishedAt());
//            Log.d("imgurl",article.getImageUrl());
            Picasso.get().load(article.getImageUrl()).placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, publishedAtTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            publishedAtTextView = itemView.findViewById(R.id.published_at_text_view);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}

