package com.example.jokes_application.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.jokes_application.MainActivity;
import com.example.jokes_application.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FriendRequestCloudNotification extends FirebaseMessagingService {

    private static final String TAG = "Firebase messaging service";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.e(TAG,"Refresh Token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String senderUid = data.get("senderUid");
            String title = data.get("title");
            String body = data.get("body");

            // Fetch sender's information from Firebase Realtime Database
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("FriendRequests");//.child(senderUid);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get user's information
                        String senderName = snapshot.child("name").getValue(String.class);

                        // Display the notification with user information
                        // You can show the notification or update your UI here
                        showNotification(senderName, title, body);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                    Toast.makeText(getApplicationContext(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void showNotification(String senderName,String title, String message) {




        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "friend_requests"; // Notification channel ID (create if not exists)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Friend Requests", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class); // Specify the target activity when the notification is clicked
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                //.setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("New Friend Request from " + senderName)
                .setContentText(title + ": " + message)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, notificationBuilder.build());

    }

}
