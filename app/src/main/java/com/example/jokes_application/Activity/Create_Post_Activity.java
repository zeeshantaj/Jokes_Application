package com.example.jokes_application.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

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
                    hashMap.put("joke",joke);
                    hashMap.put("postedDateTime",formattedTime);
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
}