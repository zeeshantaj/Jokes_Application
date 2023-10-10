package com.example.jokes_application.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jokes_application.MainActivity;
import com.example.jokes_application.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Create_Post_Activity extends AppCompatActivity {

    private String uid;
    private EditText jokeText;
    private CircleImageView userImage;
    private TextView nameTxt;
    private Button postBtn;
    private DatabaseReference reference,userInfoReference;
    private FirebaseAuth auth;
    private HashMap<String,String> hashMap = new HashMap<>();

    private CardView post_bg1,post_bg2,post_bg3, post_bg4,post_bg5,post_bg6,post_bg7,post_bg8,post_bg9,post_bg10,post_bg11,post_bg12;
    private String toolBarTitle="Create Post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(toolBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        jokeText = findViewById(R.id.createPostEdittext);
        nameTxt = findViewById(R.id.createPostUserName);
        userImage = findViewById(R.id.createPostUserImage);
        postBtn = findViewById(R.id.postBtn);
        auth = FirebaseAuth.getInstance();
         uid = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("posts");


        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM:dd:yyyy hh:mm:a",Locale.US);
        String formattedTime = simpleDateFormat.format(currentTime);

        Calendar calendar = Calendar.getInstance();
        long timeMillis= calendar.getTimeInMillis();
        String postChild = String.valueOf(timeMillis);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String joke = jokeText.getText().toString();
                if (joke.isEmpty()){
                    jokeText.setError("You can not post empty field");
                }
                else {

                    String backgroundColor = getEditTextBackgroundTintHexCode();

                    hashMap.put("joke",joke);
                    hashMap.put("postedDateTime",formattedTime);
                    hashMap.put("backgroundColor",backgroundColor);
                    hashMap.put("react","");

                    reference.child(postChild).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Create_Post_Activity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Create_Post_Activity.this, MainActivity.class));
                            //finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Create_Post_Activity.this, "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
        getUserInfo();
        changeBackgroundColor();
    }
    private void getUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String userName = sharedPreferences.getString("user_name", "");
        String userImageUrl = sharedPreferences.getString("user_image_url", "");

        nameTxt.setText(userName);
        Glide.with(Create_Post_Activity.this)
                .load(userImageUrl)
                .into(userImage);
    }

    private void changeBackgroundColor() {
        post_bg1=findViewById(R.id.postColor1);
        post_bg2=findViewById(R.id.postColor2);
        post_bg3=findViewById(R.id.postColor3);
        post_bg4=findViewById(R.id.postColor4);
        post_bg5=findViewById(R.id.postColor5);
        post_bg6=findViewById(R.id.postColor6);
        post_bg7=findViewById(R.id.postColor7);
        post_bg8=findViewById(R.id.postColor8);
        post_bg9=findViewById(R.id.postColor9);
        post_bg10=findViewById(R.id.postColor10);
        post_bg11=findViewById(R.id.postColor11);
        post_bg12=findViewById(R.id.postColor12);

        post_bg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.startColor);

            }
        });
        post_bg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post1);
            }
        });
        post_bg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post2);

            }
        });
        post_bg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post3);

            }
        });
        post_bg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post4);

            }
        });
        post_bg6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post5);

            }
        });
        post_bg7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post6);

            }
        });
        post_bg8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post7);

            }
        });
        post_bg9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post8);

            }
        });
        post_bg10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post9);

            }
        });
        post_bg11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post10);

            }
        });
        post_bg12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditTextBackgroundTint(R.color.post11);

            }
        });

    }


    private void changeEditTextBackgroundTint(int colorResId) {
        int color = ContextCompat.getColor(this, colorResId); // Get color from resources
        jokeText.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private String getEditTextBackgroundTintHexCode() {
        ColorStateList colorStateList = jokeText.getBackgroundTintList();

        if (colorStateList != null) {
            int defaultColor = colorStateList.getDefaultColor();
            return String.format("#%06X", (0xFFFFFF & defaultColor));
        } else {
            // Handle the case when there is no backgroundTint set
            return "";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

}