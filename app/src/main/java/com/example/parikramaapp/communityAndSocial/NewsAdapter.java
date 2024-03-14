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
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<Article> {

    private Context context;
    private List<Article> articles;

    public NewsAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);
        }

        Article article = articles.get(position);

        TextView titleTextView = convertView.findViewById(R.id.title_text_view);
        TextView descriptionTextView = convertView.findViewById(R.id.description_text_view);
        ImageView imageView = convertView.findViewById(R.id.image_view);

        titleTextView.setText(article.getTitle());
        descriptionTextView.setText(article.getDescription());
        Picasso.get().load(article.getImageUrl()).into(imageView); // You can use any image loading library here

        return convertView;
    }
}
