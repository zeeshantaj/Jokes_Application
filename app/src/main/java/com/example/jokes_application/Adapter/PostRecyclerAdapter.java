package com.example.jokes_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.jokes_application.Activity.Create_Post_Activity;
import com.example.jokes_application.Activity.User_Profile_Activity;
import com.example.jokes_application.Fragment.HomeFragment;
import com.example.jokes_application.FragmentUtils.FragmentUtils;
import com.example.jokes_application.Interfaces.BottomSheetListener;
import com.example.jokes_application.Model.Post;
import com.example.jokes_application.R;
import com.example.jokes_application.Reactions.Post_Reactions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {

    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<Post> postList;
    private Context context;

    private BottomSheetListener bottomSheetListener;


    public PostRecyclerAdapter(List<Post> postList, Context context,BottomSheetListener bottomSheetListener) {
        this.postList = postList;
        this.context = context;
        this.bottomSheetListener = bottomSheetListener;
    }

    @NonNull
    @Override
    public PostRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joke_layout,parent,false);
//        return new ViewHolder(view);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_POST) {
            View postView = inflater.inflate(R.layout.create_post_layout, parent, false);
            return new PostViewHolder(postView);
        } else {
            View itemView = inflater.inflate(R.layout.joke_layout, parent, false);
            return new ViewHolder(itemView);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerAdapter.ViewHolder holder, int position) {


//
        if (position == 0) {
            // Bind the post data for the first item in the RecyclerView
             ((PostViewHolder) holder).bindPostData();
        } else {
            // Bind regular item data for other positions
            ((ViewHolder) holder).bindRegularData(postList.get(position - 1));
        }

    }

    @Override
    public int getItemCount() {
        return postList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_POST : VIEW_TYPE_ITEM;
    }

    // ViewHolder for the post layout
    public class PostViewHolder extends ViewHolder {
        private CircleImageView imageView;
        private LinearLayout layout;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.userImageInPostLay);
            layout = itemView.findViewById(R.id.postJokeLinearLayout);
            // Initialize other views for the post layout as needed
        }

        public void bindPostData() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userImageUrl = sharedPreferences.getString("user_image_url", "");

            Glide.with(context)
                    .load(userImageUrl)
                    .into(imageView);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context.getApplicationContext(), Create_Post_Activity.class);
                    context.startActivity(intent);

                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, joke, postedDateTime,optionMenuText;
        private CircleImageView imageView;
        private ReactButton reactButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recyclerUserName);
            optionMenuText = itemView.findViewById(R.id.optionMenuText);
            joke = itemView.findViewById(R.id.jokeText);
            postedDateTime = itemView.findViewById(R.id.postedDateTime);
            imageView = itemView.findViewById(R.id.userImage);
            reactButton = itemView.findViewById(R.id.reactionBtn);


        }
        public void bindRegularData(Post post) {


            name.setText(post.getName());
            joke.setText(post.getJoke());
            postedDateTime.setText(post.getPostedDateTime());

            Glide.with(context)
                            .load(post.getImageUrl())
                                    .into(imageView);


            reactButton.setReactions(Post_Reactions.reactions);
            reactButton.setDefaultReaction(Post_Reactions.defaultReact);
            reactButton.setEnableReactionTooltip(true);

            reactButton.setReactionDialogShape(R.drawable.react_dialogue_shape);

            reactButton.setOnReactionChangeListener(new ReactButton.OnReactionChangeListener() {
                @Override
                public void onReactionChange(Reaction reaction) {

                    Toast.makeText(context, post.getPostId(), Toast.LENGTH_SHORT).show();

                    //setReactionToDB(post.getUid(),post.getPostId(),reaction.getReactText());

                }
            });

            reactButton.setOnReactionDialogStateListener(new ReactButton.OnReactionDialogStateListener() {
                @Override
                public void onDialogOpened() {
                  }

                @Override
                public void onDialogDismiss() {

                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String currentUid = auth.getUid();
//                    SharedPreferences IDPreference = context.getSharedPreferences("recipientIDPreference", Context.MODE_PRIVATE);
//
//                    String recipientId1 = IDPreference.getString("recipientID", "");
//                    if (currentUid.equals(recipientId1)){
//
//                    }
//                    else {


                    String uid = post.getUid();
                    if (uid.equals(currentUid)){
                        ViewPager viewPager = itemView.getRootView().findViewById(R.id.myViewPager);
                        viewPager.setCurrentItem(3, true);
                    }
                    else {
                        Intent intent = new Intent(context.getApplicationContext(), User_Profile_Activity.class);
                        intent.putExtra("UID", post.getUid());
                        context.startActivity(intent);
                    }

//                    SharedPreferences sharedPreferences = context.getSharedPreferences("AdapterSharedPreference", Context.MODE_PRIVATE);
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                    editor.putString("clicked_userName",post.getName());
//                    editor.putString("clicked_userImageUrl",post.getImageUrl());
//                    editor.putString("clicked_userId",post.getUid());


                }
            });
            optionMenuText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
//                    PopupMenu popupMenu = new PopupMenu(context,optionMenuText);
//                    popupMenu.inflate(R.menu.joke_item_menu);
//
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            int id = item.getItemId();
//                            if (id == R.id.menuSavePost) {
//                                Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
//                            }
//                            if (id == R.id.menuDontWantToSee) {
//                                Toast.makeText(context, "dont", Toast.LENGTH_SHORT).show();
//                            }
//                            if (id == R.id.menuReportUser) {
//                                Toast.makeText(context, "report", Toast.LENGTH_SHORT).show();
//                            }
//                            return false;
//                        }
//                    });
//                    popupMenu.show();


                    bottomSheetListener.onBottomSheetClicked();


                }
            });
        }
    }


    private void setReactionToDB(String UID,String postID,String reaction) {
        //not done yet
        DatabaseReference reactReference = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("posts").child(postID);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("react",reaction);

        reactReference.updateChildren(Collections.unmodifiableMap(hashMap));
        Toast.makeText(context, "ReactionSet", Toast.LENGTH_SHORT).show();
        


    }
}
