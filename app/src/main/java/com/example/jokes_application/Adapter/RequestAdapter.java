package com.example.jokes_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jokes_application.Model.RequestModel;
import com.example.jokes_application.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<RequestModel> requestModelList;
    private Context context;

    public RequestAdapter(List<RequestModel> requestModelList, Context context) {
        this.requestModelList = requestModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        RequestModel requestModel = requestModelList.get(position);
        holder.name.setText(requestModel.getName());
        Glide.with(context)
                .load(requestModel.getImageUrl())
                .into(holder.imageView);


        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView imageView;
        private Button acceptBtn,rejectBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.requestUserName);
            imageView = itemView.findViewById(R.id.requestUserImage);
            acceptBtn = itemView.findViewById(R.id.requestAcceptBtn);
            rejectBtn = itemView.findViewById(R.id.requestRejectBtn);
        }
    }
}
