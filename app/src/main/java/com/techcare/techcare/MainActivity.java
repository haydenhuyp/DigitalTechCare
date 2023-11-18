package com.techcare.techcare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.json.*;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    protected String latestMassVideoURL;
    private final String DAILY_TV_MASS_CHANNEL_ID = "UCi6JtCVy4XKu4BSG-AE2chg";

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

        /* This piece is the unfactorized version of the code above, if nothings changes, please delete this on/after Nov 20th */
        /*new Thread(() -> {

            String jsonString = null;
            try {
                jsonString = YoutubeUtility.getLatestVideosFromChannel(DAILY_TV_MASS_CHANNEL_ID).toString();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // Parse JSON string
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Extract videoIds and titles
            JSONArray items = null;
            try {
                items = jsonObject.getJSONArray("items");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            YoutubeVideo[] videos = new YoutubeVideo[items.length()];
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = null;
                try {
                    item = items.getJSONObject(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                JSONObject id = null;
                try {
                    id = item.getJSONObject("id");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String videoId = null;
                try {
                    videoId = id.getString("videoId");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String title = null;
                try {
                    title = item.getJSONObject("snippet").getString("title");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                videos[i] = new YoutubeVideo(videoId, title);
            }

            for (int i = 0; i < items.length(); i++) {
                if (videos[i].getVideoTitle().contains("Daily") && videos[i].getVideoTitle().contains("Mass")){
                    latestMassVideoURL = videos[i].getVideoUrl();
                    break;
                }
            }
        }).start();*/

        findViewById(R.id.btn_call).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_youtube).setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("latestMassVideoURL", latestMassVideoURL);
            startActivity(intent);
        });

        findViewById(R.id.btn_sudoku).setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });


        /* Firebase is temporarily disabled to preserve quota */
        /* Firebase test *//*
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
                                GridCell gridCell = new GridCell(_id, title, icon, action, actionParameter);
                                gridCells.add(gridCell);
                            }

                            updateGridCells();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // End of Firebase test
    }
    *//**
     * Set 4th cell according to the data from Firebase
     *
     * Update the 4th cell according to the data from Firebase
     * This method should be called after populating the GridCells list with data from Firestore.
     * It sets the text of a TextView and, if needed, an image in a CardView's child views.
     * @Postcondition: The 4th cell is updated with the data from Firebase.
     **//*
    private void updateGridCells() {

        CardView cardView = findViewById(R.id.cell4);
        int currentIndex = 0; // index for accessing the GridCells array list

        if (cardView != null) {
            if (cardView.getChildCount() > 0) {
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
                            myTextView.setText(gridCells.get(currentIndex).getTitle());
                            myImageView.setForeground(getDrawable(getResources().getIdentifier(gridCells.get(currentIndex).getIcon(), "drawable", getPackageName())));
                        }
                    }
                }
            }
        }*/
    }
}
