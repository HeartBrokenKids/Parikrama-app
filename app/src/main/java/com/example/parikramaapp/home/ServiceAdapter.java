package com.example.parikramaapp.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;

import java.util.List;
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private List<String> services;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    ServiceAdapter(Context context, List<String> services) {
        this.inflater = LayoutInflater.from(context);
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String service = services.get(position);
        holder.tvCardTitle.setText(service);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCardTitle;

        ViewHolder(View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.tv_card_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
