package com.techcare.techcare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import static com.techcare.techcare.DataUtility.APP_ID;
import static com.techcare.techcare.DataUtility.APP_SIGN;
import static com.techcare.techcare.DataUtility.currentUserID;
import static com.techcare.techcare.DataUtility.currentUserName;
import static com.techcare.techcare.DataUtility.targetUserID;
import static com.techcare.techcare.DataUtility.targetUserName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoCallInvitationData;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallConfigProvider;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.*;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static volatile ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    protected String latestMassVideoURL = "";
    private final String DAILY_TV_MASS_CHANNEL_ID = "UCi6JtCVy4XKu4BSG-AE2chg";

    // works temporarily should be changed
    private int currentIndexOfGrid3InGridCells = 0;
    private int currentIndexOfGrid4InGridCells = 1;
    private ZegoSendCallInvitationButton btnZegoCallSendInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keeps screen on

        // start thread to get the latest mass video URL from YoutubeDataAPI
        new Thread(() -> {
            try{
                String jsonString = YoutubeUtility.getLatestVideosFromChannel(DAILY_TV_MASS_CHANNEL_ID).toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray items = jsonObject.getJSONArray("items");

                YoutubeVideo[] videos = new YoutubeVideo[items.length()];

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject id = item.getJSONObject("id");
                    String videoId = id.getString("videoId");
                    String title = item.getJSONObject("snippet").getString("title");

                    YoutubeVideo video = new YoutubeVideo(videoId, title);
                    videos[i] = video;
                }

                for (YoutubeVideo video : videos) {
                    if (video.getVideoTitle().contains("Daily") && video.getVideoTitle().contains("Mass")) {
                        latestMassVideoURL = video.getVideoUrl();
                        break;
                    }
                }
            }
            catch (IOException | GeneralSecurityException | JSONException e) {
                Log.e(TAG, "Error getting latest mass video URL from YoutubeDataAPI: ", e);
            }
        }).start();

        // get date time and format it like this: Dec 01, 2023. 10:10 AM
        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(DateTimeUtility.getCurrentDateTime());

        findViewById(R.id.cell1).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });

        startCallInvitationService();
        initSendCallInvitationButton();
        findViewById(R.id.cell2).setOnClickListener(v -> {
            btnZegoCallSendInvitation.performClick();
        });

        findViewById(R.id.cell3).setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("latestMassVideoURL", latestMassVideoURL);
            if (gridCells.size() > currentIndexOfGrid3InGridCells) {
                intent.putExtra("youtube_url", gridCells.get(currentIndexOfGrid3InGridCells).getActionParameter().toString());
            }

            startActivity(intent);
        });

        findViewById(R.id.cell4).setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_weather).setOnClickListener(v -> {
            Intent intent = new Intent(this, StorageActivity.class);
            startActivity(intent);
        });

        /* The overlaying notification */
        findViewById(R.id.mainImgOverlayingNotification).setOnClickListener(v -> {
            // make the mainImgOverlayingNotification disappear
            findViewById(R.id.mainImgOverlayingNotification).setVisibility(View.GONE);
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gridCells")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // process each document (gridCell data) and add to the gridCells list
                                String icon = document.getString("icon");
                                int _id = document.get("_id", Integer.TYPE).intValue();
                                String title = document.getString("title");
                                String actionParameter = document.getString("actionParameter");
                                String action = document.getString("action");
                                GridCell gridCell = new GridCell(_id, title, icon, actionParameter, action);
                                gridCells.add(gridCell);
                            }
                            updateGridCells();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < gridCells.size(); i++) {
                                        Log.w(TAG, "gridCell: " + gridCells.get(i).toString());
                                    }
                                    // put updateGridCells() here if it doesn't work

                                }
                            });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        final DocumentReference docRef = db.collection("gridCells").document("grid3");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    db.collection("gridCells")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // process each document (gridCell data) and update to the gridCells list
                                            String icon = document.getString("icon");
                                            int _id = document.get("_id", Integer.TYPE).intValue();
                                            String title = document.getString("title");
                                            String actionParameter = document.getString("actionParameter");
                                            String action = document.getString("action");
                                            GridCell gridCell = new GridCell(_id, title, icon, actionParameter, action);
                                            if (gridCell.get_id() == 3) {
                                                gridCells.set(currentIndexOfGrid3InGridCells, gridCell);
                                            } else if (gridCell.get_id() == 4) {
                                                gridCells.set(currentIndexOfGrid4InGridCells, gridCell);
                                            }
                                        }
                                        updateGridCells();
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void updateGridCells() {
        CardView cardView3 = findViewById(R.id.cell3);
        CardView cardView4 = findViewById(R.id.cell4);
        int currentIndex = 0;
        for (int i = 0; i < gridCells.size(); i++) {
            if (gridCells.get(i).get_id() == 3) {
                // update cardView3 with the data from Firebase
                if (cardView3 != null) {
                    if (cardView3.getChildCount() > 0) {
                        // first child is a LinearLayout
                        View childView = cardView3.getChildAt(0);

                        if (childView instanceof LinearLayout) {
                            LinearLayout linearLayout = (LinearLayout) childView;

                            if (linearLayout.getChildCount() >= 2) {
                                View imageView = linearLayout.getChildAt(0);
                                View textView = linearLayout.getChildAt(1);

                                if (imageView instanceof ImageView && textView instanceof TextView) {
                                    ImageView myImageView = (ImageView) imageView;
                                    TextView myTextView = (TextView) textView;
                                    myTextView.setText(gridCells.get(currentIndex).getTitle());
                                    myImageView.setForeground(getDrawable(getResources().getIdentifier(gridCells.get(currentIndex).getIcon(), "drawable", getPackageName())));
                                }
                            }
                        }
                    }
                    currentIndex++;
                }
            } else if (gridCells.get(i).get_id() == 4) {
                if (cardView4 != null) {
                    if (cardView4.getChildCount() > 0) {
                        // first child is a LinearLayout
                        View childView = cardView4.getChildAt(0);

                        if (childView instanceof LinearLayout) {
                            LinearLayout linearLayout = (LinearLayout) childView;

                            if (linearLayout.getChildCount() >= 2) {
                                View imageView = linearLayout.getChildAt(0);
                                View textView = linearLayout.getChildAt(1);

                                if (imageView instanceof ImageView && textView instanceof TextView) {
                                    ImageView myImageView = (ImageView) imageView;
                                    TextView myTextView = (TextView) textView;
                                    myTextView.setText(gridCells.get(currentIndex).getTitle());
                                    myImageView.setForeground(getDrawable(getResources().getIdentifier(gridCells.get(currentIndex).getIcon(), "drawable", getPackageName())));
                                }
                            }
                        }
                    }
                    currentIndex++;
                }
            }
        }
    }
    private void startCallInvitationService(){
        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        // not sure why this isn't working, maybe from Zegocloud's side
        /* callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true; */

        callInvitationConfig.provider = new ZegoUIKitPrebuiltCallConfigProvider() {
            @Override
            public ZegoUIKitPrebuiltCallConfig requireConfig(ZegoCallInvitationData invitationData) {
                ZegoUIKitPrebuiltCallConfig config = null;
                boolean isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.getValue();
                boolean isGroupCall = invitationData.invitees.size() > 1;
                if (isVideoCall && isGroupCall) {
                    config = ZegoUIKitPrebuiltCallConfig.groupVideoCall();
                } else if (!isVideoCall && isGroupCall) {
                    config = ZegoUIKitPrebuiltCallConfig.groupVoiceCall();
                } else if (!isVideoCall) {
                    config = ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall();
                } else {
                    config = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall();
                }
                config.topMenuBarConfig.isVisible = true;
                config.topMenuBarConfig.buttons.add(ZegoMenuBarButtonName.MINIMIZING_BUTTON);
                return config;
            }
        };

        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), APP_ID, APP_SIGN, currentUserID, currentUserName, callInvitationConfig);
    }

    private void initSendCallInvitationButton(){
        Context context = getApplicationContext();

        btnZegoCallSendInvitation = new ZegoSendCallInvitationButton(context);
        // If true, a video call. Otherwise, a voice call is made.
        btnZegoCallSendInvitation.setIsVideoCall(true);
        btnZegoCallSendInvitation.setResourceID("zego_uikit_call");

        btnZegoCallSendInvitation.setOnClickListener(v -> {
            btnZegoCallSendInvitation.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserID, targetUserName)));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}
