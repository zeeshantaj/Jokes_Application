package com.example.jokes_application.Adapter;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.jokes_application.Model.Post;
import com.example.jokes_application.R;
import com.example.jokes_application.Reactions.Post_Reactions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    private List<Post> postList;
    private Context context;

    public UserProfileAdapter(List<Post> postList,Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joke_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.name.setText(post.getName());
        holder.joke.setText(post.getJoke());
        holder.postedDateTime.setText(post.getPostedDateTime());
        Glide.with(context)
                .load(post.getImageUrl())
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, joke, postedDateTime;
        private CircleImageView imageView;
        private ReactButton reactButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recyclerUserName);
            joke = itemView.findViewById(R.id.jokeText);
            postedDateTime = itemView.findViewById(R.id.postedDateTime);
            imageView = itemView.findViewById(R.id.userImage);
            reactButton = itemView.findViewById(R.id.reactionBtn);


            reactButton.setReactions(Post_Reactions.reactions);
            reactButton.setDefaultReaction(Post_Reactions.defaultReact);
            reactButton.setEnableReactionTooltip(true);

            reactButton.setReactionDialogShape(R.drawable.react_dialogue_shape);

            reactButton.setOnReactionChangeListener(new ReactButton.OnReactionChangeListener() {
                @Override
                public void onReactionChange(Reaction reaction) {
                    Toast.makeText(context.getApplicationContext(), "ReactionChange" + reaction.getReactText(), Toast.LENGTH_SHORT).show();
                }
            });

            reactButton.setOnReactionDialogStateListener(new ReactButton.OnReactionDialogStateListener() {
                @Override
                public void onDialogOpened() {
                    Toast.makeText(context.getApplicationContext(), "ReactionDialog", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDialogDismiss() {

                }
            });

        }
    }
}
