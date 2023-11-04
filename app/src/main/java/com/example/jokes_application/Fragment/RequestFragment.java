package com.example.jokes_application.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jokes_application.Adapter.RequestAdapter;
import com.example.jokes_application.Model.RequestModel;
import com.example.jokes_application.R;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {
    public RequestFragment() {
        // Required empty public constructor
    }

    private View view;
    private DatabaseReference requestReference;
    private FirebaseAuth auth;
    private String currentUid,senderId;
    private List<RequestModel> requestModelList;
    private RequestAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request, container, false);

        recyclerView = view.findViewById(R.id.requestRecycler);
        requestModelList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        currentUid = auth.getUid();

        requestReference = FirebaseDatabase.getInstance().getReference("FriendRequests");
        requestReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestModelList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    senderId = dataSnapshot.child("recipientId").getValue(String.class);
                    if (currentUid.equals(senderId)){
                        RequestModel requestModel = dataSnapshot.getValue(RequestModel.class);
                        requestModelList.add(requestModel);

                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RequestAdapter(requestModelList,getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }
}