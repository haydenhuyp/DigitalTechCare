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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.components.audiovideo.ZegoBaseAudioVideoForegroundView;
import com.zegocloud.uikit.components.audiovideo.ZegoForegroundViewProvider;
import com.zegocloud.uikit.components.common.ZegoButton;
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

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.*;

public class MainActivity extends AppCompatActivity {
    private static volatile ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    protected String latestMassVideoURL = "";
    private final String DAILY_TV_MASS_CHANNEL_ID = "UCi6JtCVy4XKu4BSG-AE2chg";
    private ZegoSendCallInvitationButton btnZegoCallSendInvitation;
    private StorageReference storageReference;
    private ImageView mainImgOverlayingNotification;
    private TextView mainTextOverlayingNotification;
    private LinearLayout overlayingNotification;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Bitmap menuImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keeps screen on

        // get the latest mass video URL from YoutubeDataAPI
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
                        // write to Firebase
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection("gridCells").document("grid3");
                        docRef.update("actionParameter", latestMassVideoURL);
                        break;
                    }
                }
            }
            catch (IOException | GeneralSecurityException | JSONException e) {
                Log.e(TAG, "Error getting latest mass video URL from YoutubeDataAPI: ", e);
            }
        }).start();
        mainImgOverlayingNotification = findViewById(R.id.mainImgOverlayingNotification);
        mainTextOverlayingNotification = findViewById(R.id.mainTextOverlayingNotification);
        overlayingNotification = findViewById(R.id.overlayingNotification);
        mainImgOverlayingNotification.setVisibility(View.GONE);
        mainTextOverlayingNotification.setVisibility(View.GONE);
        overlayingNotification.setVisibility(View.GONE);
        new Thread(() -> {
            // TODO: change this ImageID
            String imageID = "menu";

            storageReference = FirebaseStorage.getInstance().getReference("images/" + imageID /*+ ".png"*/);
            final File localFile;
            try {
                localFile = File.createTempFile("tempFile", ".png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                menuImage = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                mainImgOverlayingNotification.setImageBitmap(menuImage);
                mainImgOverlayingNotification.setVisibility(View.VISIBLE);
                mainTextOverlayingNotification.setVisibility(View.VISIBLE);
                overlayingNotification.setVisibility(View.VISIBLE);
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to download image: " + e, Toast.LENGTH_SHORT).show();
            });
        }).start();

        // Date time format: Dec 01, 2023. 10:10 AM
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView txtDate = findViewById(R.id.txtDate);
                txtDate.setText(DateTimeUtility.getCurrentDateTime());
                // update every second (1000 milliseconds)
                handler.postDelayed(this, 1000);
            }
        }, 1000);

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
            if (gridCells.size() > 0) {
                intent.putExtra("youtube_url", gridCells.get(2).getActionParameter().toString());
            }
            startActivity(intent);
        });

        findViewById(R.id.cell4).setOnClickListener(v -> {
            /*Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);*/
            if (menuImage != null) {
                mainImgOverlayingNotification.setImageBitmap(menuImage);
                overlayingNotification.setVisibility(View.VISIBLE);
                mainImgOverlayingNotification.setVisibility(View.VISIBLE);
                mainTextOverlayingNotification.setVisibility(View.VISIBLE);

            }
        });

        findViewById(R.id.btn_weather).setOnClickListener(v -> {
            Intent intent = new Intent(this, StorageActivity.class);
            startActivity(intent);
        });

        /* The overlaying notification */
        mainImgOverlayingNotification.setOnClickListener(v -> {
            overlayingNotification.setVisibility(View.GONE);
        });

        /*
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
        });*/
        /* Update the grid cells with the data from Firebase */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gridCells")
                .get()
                .addOnCompleteListener(task -> {
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
                    } else {
                        Log.e(TAG, "Error getting data from Firebase: ", task.getException());
                    }
                });
        /* Update the cells every time the data in Firebase changes */
        db.collection("gridCells")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Firebase listen failed: ", error);
                        return;
                    }

                    if (value != null) {
                        db.collection("gridCells")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // process each document (gridCell data) and update to the gridCells list
                                            String icon = document.getString("icon");
                                            int _id = document.get("_id", Integer.TYPE).intValue();
                                            String title = document.getString("title");
                                            String actionParameter = document.getString("actionParameter");
                                            String action = document.getString("action");
                                            GridCell gridCell = new GridCell(_id, title, icon, actionParameter, action);
                                            gridCells.set(gridCell.get_id() - 1, gridCell);
                                        }
                                        updateGridCells();
                                    } else {
                                        Log.e(TAG, "Error getting updated data from Firebase: ", task.getException());
                                    }
                                });
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                });
    }

    /**
     * Update the grid cells on the main activity
     */
    private void updateGridCells() {
        CardView cardView1 = findViewById(R.id.cell1);
        CardView cardView2 = findViewById(R.id.cell2);
        CardView cardView3 = findViewById(R.id.cell3);
        CardView cardView4 = findViewById(R.id.cell4);
        for (int i = 0; i < gridCells.size(); i++) {
            switch (gridCells.get(i).get_id()){
                case 1:
                    /* Hard-coded text to fix the \n issue - Firebase could not process \n correctly */
                    updateCardView(cardView1, "Community\nRoom", gridCells.get(i).getIcon());
                    break;
                case 2:
                    updateCardView(cardView2, gridCells.get(i).getTitle(), gridCells.get(i).getIcon());
                    break;
                case 3:
                    updateCardView(cardView3, gridCells.get(i).getTitle(), gridCells.get(i).getIcon());
                    break;
                case 4:
                    updateCardView(cardView4, gridCells.get(i).getTitle(), gridCells.get(i).getIcon());
                    break;
            }
        }
    }

    /**
     * Update the cardView with the given title and icon
     * @param cardView one of the cardView buttons on main activity
     * @param title
     * @param icon the icon name in drawable, e.g. "ic_church"
     */
    private void updateCardView(CardView cardView, String title, String icon){
        if (cardView == null) return;
        // cardView doesn't have any child, return
        if (cardView.getChildCount() == 0) return;

        // first child is a LinearLayout
        View childView = cardView.getChildAt(0);

        if (childView instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) childView;

            if (linearLayout.getChildCount() >= 2) {
                View imageView = linearLayout.getChildAt(0);
                View textView = linearLayout.getChildAt(1);

                if (imageView instanceof ImageView && textView instanceof TextView) {
                    ImageView myImageView = (ImageView) imageView;
                    TextView myTextView = (TextView) textView;
                    myTextView.setText(title);
                    myImageView.setForeground(getDrawable(getResources()
                            .getIdentifier(icon, "drawable", getPackageName())));
                }
            }
        }
    }
    private void startCallInvitationService(){
        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
         // not sure why this isn't working, maybe from Zegocloud's side
         // callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;

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
                config.bottomMenuBarConfig.buttons.add(ZegoMenuBarButtonName.HANG_UP_BUTTON);

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
        handler.removeCallbacksAndMessages(null);
        ZegoUIKitPrebuiltCallInvitationService.unInit();
        super.onDestroy();
    }
}
