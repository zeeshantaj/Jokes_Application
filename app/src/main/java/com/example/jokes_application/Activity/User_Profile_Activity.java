package com.example.jokes_application.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.jokes_application.Adapter.ProfileRecyclerAdapter;
import com.example.jokes_application.Model.UserProfileModel;
import com.example.jokes_application.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User_Profile_Activity extends AppCompatActivity {

    private ProfileRecyclerAdapter adapter;
    private List<UserProfileModel> profileModelList;
    private RecyclerView recyclerView;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Objects.requireNonNull(getSupportActionBar()).hide();
        recyclerView = findViewById(R.id.profileRecycler);
        profileModelList = new ArrayList<>();

        Intent intent = getIntent();
        String  userId = intent.getStringExtra("UID");

        SharedPreferences sharedPreferences = getSharedPreferences("recipientIDPreference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recipientID",userId);
        editor.apply();

        reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileModelList.clear();
                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("image").getValue(String.class);

                Log.e("MyApp","info"+name);
                Log.e("MyApp","info"+imageUrl);
                    DataSnapshot postSnapshot = snapshot.child("posts");
                    for (DataSnapshot dataSnapshot : postSnapshot.getChildren()) {

                        String joke = dataSnapshot.child("joke").getValue(String.class);
                        String postedDateTime = dataSnapshot.child("postedDateTime").getValue(String.class);
                        UserProfileModel post = new UserProfileModel();
                        post.setName(name);
                        post.setJoke(joke);
                        post.setPostedDateTime(postedDateTime);
                        post.setImageUrl(imageUrl);


                        SharedPreferences sharedPreferences =getSharedPreferences("ProfileInformation", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("profileName", name);
                        editor.putString("profileImageUrl", imageUrl);
                        editor.apply();

                        Log.e("MyApp","profileInfo"+joke);

                        profileModelList.add(post);
                    }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ProfileRecyclerAdapter(profileModelList,this);
        recyclerView.setAdapter(adapter);
    }
}