package com.example.parikramaapp.economicOpportunities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {

    private static final String TAG = "JobPostAdapter";
    private List<JobPost> jobPosts;
    private Context context;
    private OnUpvoteClickListener upvoteClickListener;
    private FirebaseFirestore db;

    public void setOnUpvoteClickListener(OnUpvoteClickListener listener) {
        this.upvoteClickListener = listener;
    }

    public JobPostAdapter(List<JobPost> jobPosts, Context context) {
        this.jobPosts = jobPosts;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public JobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_post, parent, false);
        return new JobPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobPostViewHolder holder, int position) {
        JobPost jobPost = jobPosts.get(position);
        holder.bind(jobPost);
    }

    @Override
    public int getItemCount() {
        return jobPosts.size();
    }

    public class JobPostViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewContactInfo;
        private Button btnUpvote; // New button for upvoting

        public JobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewContactInfo = itemView.findViewById(R.id.textViewContactInfo);
            btnUpvote = itemView.findViewById(R.id.btnUpvote); // Initialize upvote button

            // Set up click listener for upvote button
            btnUpvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the upvote method of the adapter passing the position
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        upvote(position);
                    }
                }
            });
        }

        public void bind(JobPost jobPost) {
            textViewTitle.setText(jobPost.getTitle());
            textViewDescription.setText(jobPost.getDescription());
            textViewContactInfo.setText(jobPost.getContactInfo());
            btnUpvote.setText("Upvote (" + jobPost.getUpvotes() + ")");
        }

        public void upvote(int position) {
            if (upvoteClickListener != null) {
                upvoteClickListener.onUpvoteClick(position);
            }
        }
    }

    public interface OnUpvoteClickListener {
        void onUpvoteClick(int position);
    }

    // Method to update upvote count in Firebase
    public void updateUpvoteCount(final int position) {
        JobPost jobPost = jobPosts.get(position);
        int newUpvotes = jobPost.getUpvotes() + 1;
        jobPost.setUpvotes(newUpvotes);
        db.collection("job_posts").document(jobPost.getId()).update("upvotes", newUpvotes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Upvote count updated successfully");
                        notifyDataSetChanged(); // Refresh the UI after successful upvote
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating upvote count: " + e.getMessage());
                        // Handle failure
                    }
                });
    }
}
