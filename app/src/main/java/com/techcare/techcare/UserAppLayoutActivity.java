package com.techcare.techcare;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserAppLayoutActivity extends AppCompatActivity {
    private static volatile ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    private int currentIndexOfGrid3InGridCells = 0;
    private int currentIndexOfGrid4InGridCells = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_app_layout);

        findViewById(R.id.btnAddLayout1).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout2).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout3).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout4).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChooseActionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnAddLayout5).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
        CardView cardView3 = findViewById(R.id.btnAddLayout3);
        CardView cardView4 = findViewById(R.id.btnAddLayout4);
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
    }