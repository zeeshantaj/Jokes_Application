package com.example.jokes_application.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jokes_application.Adapter.PostRecyclerAdapter;
import com.example.jokes_application.Interfaces.BottomSheetListener;
import com.example.jokes_application.Model.Post;
import com.example.jokes_application.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements BottomSheetListener {


    public HomeFragment() {
        // Required empty public constructor
    }


    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference reference,userInfoReference;

    private String joke;
    private String postedDateTime;
    private PostRecyclerAdapter adapter;
    private ShimmerFrameLayout shimmerLayout;
    private final Handler shimmerHandler = new Handler(Looper.getMainLooper());
    private static final int SHIMMER_DELAY_MS = 1000; // Adjust the delay as needed


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        recyclerView = view.findViewById(R.id.postRecycler);
        reference = FirebaseDatabase.getInstance().getReference("Users");

        List<Post> postList = new ArrayList<>();

        shimmerLayout.startShimmer();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String uid = userSnapshot.child("associatedID").getValue(String.class);
                    String name = userSnapshot.child("name").getValue(String.class);
                    String imageUrl = userSnapshot.child("image").getValue(String.class);

                    Log.e("MyApp","uid"+uid);
                    Log.e("MyApp","name"+name);
                    DataSnapshot postSnapshot = userSnapshot.child("posts");
                    for (DataSnapshot dataSnapshot : postSnapshot.getChildren()){
                        String postId = dataSnapshot.getKey();
                        Log.e("MyApp","postID"+postId);
                        joke = dataSnapshot.child("joke").getValue(String.class);
                        postedDateTime = dataSnapshot.child("postedDateTime").getValue(String.class);

                        Post post = new Post();
                        post.setName(name);
                        post.setJoke(joke);
                        post.setImageUrl(imageUrl);
                        post.setPostedDateTime(postedDateTime);
                        post.setUid(uid);
                        post.setPostId(postId);
                        postList.add(post);



                    }
                }
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new PostRecyclerAdapter(postList,getActivity(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();

        userInfoReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid);
        userInfoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("image").getValue(String.class);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("user_name", name);
                editor.putString("user_image_url", imageUrl);
                editor.apply();
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }


    private void startShimmer() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        // Schedule a delayed call to stopShimmer() after a certain duration
        shimmerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopShimmer();
            }
        }, SHIMMER_DELAY_MS);
    }

    private void stopShimmer() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBottomSheetClicked() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getChildFragmentManager(),bottomSheetFragment.getTag());
    }
}