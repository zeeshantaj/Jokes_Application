package com.example.jokes_application.Adapter;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.jokes_application.Model.Post;
import com.example.jokes_application.R;
import com.example.jokes_application.Reactions.Post_Reactions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context,holder.optionMenu);
                popupMenu.inflate(R.menu.delete_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.delete_profile_post) {


                            String postID = post.getPostId();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(uid)
                                    .child("posts")
                                    .child(postID);
                            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(context, "delete"+post.getJoke(), Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, joke, postedDateTime,optionMenu;
        private CircleImageView imageView;
        private ReactButton reactButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recyclerUserName);
            joke = itemView.findViewById(R.id.jokeText);
            postedDateTime = itemView.findViewById(R.id.postedDateTime);
            imageView = itemView.findViewById(R.id.userImage);
            reactButton = itemView.findViewById(R.id.reactionBtn);
            optionMenu = itemView.findViewById(R.id.optionMenuText);


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
