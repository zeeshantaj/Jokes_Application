package com.example.jokes_application.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jokes_application.Adapter.UserProfileAdapter;
import com.example.jokes_application.Model.Post;
import com.example.jokes_application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    private View view;
    private RecyclerView recyclerView;
    private UserProfileAdapter adapter;
    private List<Post> postList;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private TextView name;
    private CircleImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
//        if (getActivity() != null) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        }



        recyclerView = view.findViewById(R.id.userProfileRecycler);
        name = view.findViewById(R.id.nameProfile);
        imageView = view.findViewById(R.id.imageViewProfile);
        auth = FirebaseAuth.getInstance();
        postList = new ArrayList<>();
        String uid = auth.getUid();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userImageUrl = sharedPreferences.getString("user_image_url", "");
        String nameStr = sharedPreferences.getString("user_name", "");

        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        name.setText(nameStr);
        Glide.with(getActivity())
                .load(userImageUrl)
                .into(imageView);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "under process", Toast.LENGTH_SHORT).show();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "under process", Toast.LENGTH_SHORT).show();
            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    DataSnapshot postSnapshot = snapshot.child("posts");
                    for (DataSnapshot dataSnapshot : postSnapshot.getChildren()) {
                        String postID = dataSnapshot.getKey();
                        String joke = dataSnapshot.child("joke").getValue(String.class);
                        String backgroundColor = dataSnapshot.child("backgroundColor").getValue(String.class);
                        String postedDateTime = dataSnapshot.child("postedDateTime").getValue(String.class);

                        Post post = new Post();
                        post.setName(name);
                        post.setJoke(joke);
                        post.setPostId(postID);
                        post.setBackgroundColor(backgroundColor);
                        post.setImageUrl(imageUrl);
                        post.setPostedDateTime(postedDateTime);
                        Log.e("MyApp", "data" + post.getName());
                        Log.e("MyApp", "data" + post.getJoke());
                        postList.add(post);
                    }
                    adapter.notifyDataSetChanged();
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new UserProfileAdapter(postList,getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}