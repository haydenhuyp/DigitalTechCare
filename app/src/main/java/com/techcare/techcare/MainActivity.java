package com.techcare.techcare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<GridCell> gridCells = new ArrayList<GridCell>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keeps screen on

        findViewById(R.id.btn_call).setOnClickListener(v -> {
            Intent intent = new Intent(this, CallActivity.class);
            startActivity(intent);
        });
        /* Another way to do it by using webview: https://www.youtube.com/watch?v=V2KCAfHjySQ&ab_channel=EverydayProgrammer */
        findViewById(R.id.btn_youtube).setOnClickListener(v -> {
            String id = "pPKMMlrFdeM";
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        });

        /* Firebase test */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gridCells")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // separate 4 fields from the document: icon, _id, title and actionParameter
                                String icon = document.getString("icon");
                                int _id = document.get("_id", Integer.TYPE).intValue();
                                String title = document.getString("title");
                                String actionParameter = document.getString("actionParameter");
                                GridCell gridCell = new GridCell(_id, title, icon, actionParameter);
                                gridCells.add(gridCell);
                            }

                            updateGridCells();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        /* End of Firebase test */
    }
    /* Set 4th cell according to the data from Firebase */

    /**
     * Update the 4th cell according to the data from Firebase
     * This method should be called after populating the GridCells list with data from Firestore.
     * It sets the text of a TextView and, if needed, an image in a CardView's child views.
     */

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
        }
    }
}
