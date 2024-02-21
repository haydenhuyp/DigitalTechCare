package com.techcare.techcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StorageActivity extends AppCompatActivity {
    Uri imageUri;
    StorageReference storageReference;
    ImageView imageView;
    EditText txtStorageImgName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        imageView = findViewById(R.id.storageImageView);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        findViewById(R.id.btnStorageSelectImage).setOnClickListener(v -> {
            selectImage();
        });

        findViewById(R.id.btnStorageUploadImage).setOnClickListener(v -> {
            uploadImage();
        });
        
        findViewById(R.id.btnStorageRetrieve).setOnClickListener(v -> {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            txtStorageImgName = findViewById(R.id.txtStorageImgName);
            String imageID = "dtc_icon";

            storageReference = FirebaseStorage.getInstance().getReference("images/" + imageID + ".png");
            try {
                /* Download to memory */
                /*
                File localFile = File.createTempFile("tempFile", ".jpg");
                storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }).addOnFailureListener(e -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(this, "Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
                /* Download to local storage - but not see the file after download */
                File localFile = File.createTempFile("icon", "jpg");
                //StorageReference imageRef = storageReference.child("images/dtc_icon.png");

                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                        // toast
                        Toast.makeText(StorageActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(StorageActivity.this, "Error: " + exception, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                    throw new RuntimeException(e);
            };
        });
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formater.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imageView.setImageURI(null);
            Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}