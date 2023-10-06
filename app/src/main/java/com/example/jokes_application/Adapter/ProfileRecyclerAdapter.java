package com.example.jokes_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.reactbutton.ReactButton;
import com.amrdeveloper.reactbutton.Reaction;
import com.bumptech.glide.Glide;
import com.example.jokes_application.Model.RequestModel;
import com.example.jokes_application.Model.UserProfileModel;
import com.example.jokes_application.R;
import com.example.jokes_application.Reactions.Post_Reactions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder> {


    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private List<UserProfileModel> modelList;
    private Context context;

    public ProfileRecyclerAdapter(List<UserProfileModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_POST) {
            View postView = inflater.inflate(R.layout.user_profile_layout, parent, false);
            return new startLayoutHolder(postView);
        } else {
            View itemView = inflater.inflate(R.layout.joke_layout, parent, false);
            return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecyclerAdapter.ViewHolder holder, int position) {
//        UserProfileModel userProfileModel = modelList.get(position);
//        holder.name.setText(userProfileModel.getName());
//        holder.joke.setText(userProfileModel.getJoke());
//        holder.postedDateTime.setText(userProfileModel.getPostedDateTime());
//        Glide.with(context)
//                .load(userProfileModel.getImageUrl())
//                .into(holder.profileImage);
        if (position == 0) {
            // Bind the post data for the first item in the RecyclerView
            ((startLayoutHolder) holder).bindStartLayout();
        } else {
            // Bind regular item data for other positions
            ((ViewHolder) holder).bindViewHolder(modelList.get(position - 1));
        }


    }

    @Override
    public int getItemCount() {
        return modelList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_POST : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, joke, postedDateTime;
        private CircleImageView profileImage;
        private ReactButton reactButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.userImage);
            name = itemView.findViewById(R.id.recyclerUserName);
            joke = itemView.findViewById(R.id.jokeText);
            postedDateTime = itemView.findViewById(R.id.postedDateTime);
            reactButton = itemView.findViewById(R.id.reactionBtn);

        }

        public void bindViewHolder(UserProfileModel model) {
            name.setText(model.getName());
            postedDateTime.setText(model.getPostedDateTime());
            joke.setText(model.getJoke());


            Glide.with(context)
                    .load(model.getImageUrl())
                    .into(profileImage);
            reactButton.setReactions(Post_Reactions.reactions);
            reactButton.setDefaultReaction(Post_Reactions.defaultReact);
            reactButton.setEnableReactionTooltip(true);

            reactButton.setReactionDialogShape(R.drawable.react_dialogue_shape);

            reactButton.setOnReactionChangeListener(new ReactButton.OnReactionChangeListener() {
                @Override
                public void onReactionChange(Reaction reaction) {
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
        }
    }

    public class startLayoutHolder extends ViewHolder {

        private ImageView profile;
        private TextView name;
        private Button requestBtn;

        private SharedPreferences sharedPreferences, IDPreference;
        private FirebaseAuth auth;
        private String currentUid;
        private DatabaseReference reference;
        private String requestId, recipientId1,imageUrl,senderName;

        public startLayoutHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profileUserName);
            profile = itemView.findViewById(R.id.userProfileImageView);
            requestBtn = itemView.findViewById(R.id.requestBtn);
            auth = FirebaseAuth.getInstance();
            currentUid = auth.getUid();

            reference = FirebaseDatabase.getInstance().getReference("FriendRequests");

            sharedPreferences = context.getSharedPreferences("ProfileInformation", Context.MODE_PRIVATE);
            IDPreference = context.getSharedPreferences("recipientIDPreference", Context.MODE_PRIVATE);
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            imageUrl = sharedPreferences.getString("user_image_url", "");
            senderName = sharedPreferences.getString("user_name", "");
            recipientId1 = IDPreference.getString("recipientID", "");

            if (currentUid.equals(recipientId1)){
                requestBtn.setVisibility(View.GONE);
            }
        }

        public void bindStartLayout() {
            //  UserProfileModel model = modelList.get(getAdapterPosition());
            //UserProfileModel model = new UserProfileModel();
//            nameStr = model.getName();
//            imageUrlStr = model.getImageUrl();

            String nameStr = sharedPreferences.getString("profileName", "");
            String imageUrlStr = sharedPreferences.getString("profileImageUrl", "");
            name.setText(nameStr);
            Glide.with(context)
                    .load(imageUrlStr)
                    .into(profile);


            requestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestModel request = new RequestModel();
                    request.setSenderId(currentUid);
                    request.setRecipientId(recipientId1);
                    request.setName(senderName);
                    request.setImageUrl(imageUrl);
                    request.setStatus("pending"); // Set an initial status (e.g., "pending")


                    // Generate a unique key for the request
                    requestId = reference.push().getKey();

                    // Save the request to the database
                    reference.child(requestId).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Request Send Successfully", Toast.LENGTH_SHORT).show();
                            requestBtn.setText("Request Sent");
                            requestBtn.setEnabled(false);
                            requestBtn.setBackgroundColor(R.color.darkGrey);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(context, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            requestBtn.setEnabled(true);
                        }
                    });
//                    if (requestBtn.getText().equals("Request sent")){
//                        requestBtn.setText("Request to connect");
//                        //sendRequest(currentUid,recipientId1);
//
//                        // Create a new friend request
//
//
//                    }
//                    else {
//                        requestBtn.setText("Request sent");
//                    }
                }
            });
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                    if (snapshot1.exists()) {
                        for (DataSnapshot snapshot : snapshot1.getChildren()) {
                            String id = snapshot.child("recipientId").getValue(String.class);
                            if (id.equals(recipientId1)) {
                                String status = snapshot.child("status").getValue(String.class);
                                requestBtn.setText("Request Sent");
                                requestBtn.setEnabled(false);
                                requestBtn.setBackgroundColor(R.color.darkGrey);
                                if (status.equals("accepted")){
                                    requestBtn.setText("Jokeriend");
                                    requestBtn.setEnabled(false);
                                    requestBtn.setBackgroundColor(R.color.darkGrey);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    requestBtn.setEnabled(true);
                }
            });
        }
    }
//    private static final int VIEW_TYPE_POST = 0;
//    private static final int VIEW_TYPE_ITEM = 1;
//
//    private List<UserProfileModel> postList;
//    private Context context;
//
//    public ProfileRecyclerAdapter(List<UserProfileModel> postList, Context context) {
//        this.postList = postList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ProfileRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
//        if (viewType == VIEW_TYPE_POST) {
//            View postView = inflater.inflate(R.layout.user_profile_layout, parent, false);
//            return new ProfileLayoutHolder(postView);
//        } else {
//            View itemView = inflater.inflate(R.layout.joke_layout, parent, false);
//            return new ViewHolder(itemView);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ProfileRecyclerAdapter.ViewHolder holder, int position) {
//        if (position == 0) {
//            // Bind the post data for the first item in the RecyclerView
//            ((ProfileLayoutHolder) holder).bindProfileData();
//        } else {
//            // Bind regular item data for other positions
//            ((ViewHolder) holder).bindJokeData(postList.get(position - 1));
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size() + 1;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position == 0 ? VIEW_TYPE_POST : VIEW_TYPE_ITEM;
//    }
//
//    public class ProfileLayoutHolder extends ViewHolder{
//
//        private Button requestBtn;
//        private TextView name;
//        private ImageView profileImage,verificationBadge;
//
//        public ProfileLayoutHolder(@NonNull View itemView) {
//            super(itemView);
//
//            requestBtn = itemView.findViewById(R.id.requestBtn);
//            name = itemView.findViewById(R.id.profileUserName);
//            profileImage = itemView.findViewById(R.id.userProfileImageView);
//            verificationBadge = itemView.findViewById(R.id.profileVerificationBadge);
//
//        }
//
//        public void bindProfileData() {
//
//            SharedPreferences sharedPreferences = context.getSharedPreferences("AdapterSharedPreference", Context.MODE_PRIVATE);
//
//            String userName = sharedPreferences.getString("clicked_userName", "");
//            String imageUrl = sharedPreferences.getString("clicked_userImageUrl", "");
//            String userId = sharedPreferences.getString("clicked_userId", "");
//
//            Log.e("MyApp","userInfo"+userName);
//            Log.e("MyApp","userInfo"+imageUrl);
//            Log.e("MyApp","userInfo"+userId);
//            //name.setText();
//
//            name.setText(userName);
//            Glide.with(context)
//                    .load(imageUrl)
//                    .into(profileImage);
//
//        }
//    }
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView name, joke, postedDateTime;
//        private CircleImageView imageView;
//        private ReactButton reactButton;
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.recyclerUserName);
//            joke = itemView.findViewById(R.id.jokeText);
//            postedDateTime = itemView.findViewById(R.id.postedDateTime);
//            imageView = itemView.findViewById(R.id.userImage);
//            reactButton = itemView.findViewById(R.id.reactionBtn);
//        }
//
//        public void bindJokeData(UserProfileModel post) {
//
//            name.setText(post.getName());
//            joke.setText(post.getJoke());
//            postedDateTime.setText(post.getPostedDateTime());
//
//            Glide.with(context)
//                    .load(post.getImageUrl())
//                    .into(imageView);
//
//
//        }
//    }
}
