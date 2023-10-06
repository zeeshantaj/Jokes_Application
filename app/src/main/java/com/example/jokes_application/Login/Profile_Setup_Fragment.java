package com.example.jokes_application.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jokes_application.Interfaces.LoginCallBack;
import com.example.jokes_application.MainActivity;
import com.example.jokes_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Setup_Fragment extends Fragment {

    public Profile_Setup_Fragment() {
        // Required empty public constructor
    }


    private View view;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private UploadTask uploadTask;
    private StorageReference imageRef,storageRef;
    private FirebaseStorage storage;
    private CircleImageView profileImage;
    private TextInputEditText userName;
    private Button finish;
    private Uri selectedImageUri;
    private String uid;
    private LoginCallBack loginCallBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile__setup_, container, false);

        profileImage = view.findViewById(R.id.setupProfileImage);
        userName = view.findViewById(R.id.userNameEditText);
        finish = view.findViewById(R.id.finishBtn);

        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        String imageName = "image_" + uid + ".jpg";
        imageRef = storageRef.child("UserImages/"+ imageName);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage.setOnClickListener((v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageLauncher.launch(intent);
        }));

        finish.setOnClickListener((v -> {
            String name = userName.getText().toString();
            HashMap<String, String> value = new HashMap<>();
            if (!name.isEmpty()){

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageRef.getDownloadUrl().addOnSuccessListener(uir -> {

                            String image = uir.toString();
                            value.put("name", name);
                            value.put("image", image);
                            value.put("associatedID", uid);

                            reference.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("MyApp", "Error " + e.getLocalizedMessage());
                                    }
                            });

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("MyApp", "Error " + e.getLocalizedMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MyApp", "Error " + e.getLocalizedMessage());
                        Toast.makeText(getActivity(), "Error "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else {
                Toast.makeText(getActivity(), "Name field is require", Toast.LENGTH_SHORT).show();
            }
        }));

    }
    private ActivityResultLauncher<Intent> imageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    selectedImageUri = result.getData().getData();
                    profileImage.setImageURI(selectedImageUri);
                    uploadTask = imageRef.putFile(selectedImageUri);
                }
            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginCallBack) {
            loginCallBack = (LoginCallBack) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement LoginCallback");
        }
        loginCallBack.onStepChanged(3);

        Log.e("MyApp","ProfileFragment");

    }
}