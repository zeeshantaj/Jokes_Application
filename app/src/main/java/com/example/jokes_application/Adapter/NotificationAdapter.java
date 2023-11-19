package com.example.jokes_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jokes_application.Model.NotificationDetails;
import com.example.jokes_application.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationDetails> notificationDetails;
    private Context context;

    public NotificationAdapter(List<NotificationDetails> notificationDetails, Context context) {
        this.notificationDetails = notificationDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationDetails notificationDetails1 = notificationDetails.get(position);
        holder.name.setText(notificationDetails1.getName());
        holder.msg.setText(notificationDetails1.getMessage());

        Glide.with(context)
                .load(notificationDetails1.getImageUrl())
                .into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return notificationDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userImage;
        private TextView name,msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.notificationUserImage);
            name = itemView.findViewById(R.id.notificationUserName);
            msg = itemView.findViewById(R.id.notificationMessageTxt);
        }
    }
}
