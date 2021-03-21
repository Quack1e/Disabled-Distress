package com.martin.training.disabledetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageSelectActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 100;
    private boolean IMAGE_SELECTED = false;

    Button btnContinue;
    ImageButton btnOpenimage;
    ImageView ivPreview;
    TextView tvDescImage;
    String uriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        btnContinue = findViewById(R.id.BTNcontinue);
        btnOpenimage = findViewById(R.id.BTNaddImage);
        ivPreview = findViewById(R.id.IVpreview);
        tvDescImage = findViewById(R.id.TVImageDescription);

        btnOpenimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImages(v);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMAGE_SELECTED){
                    Intent next = new Intent(ImageSelectActivity.this, IssuesActivity.class);
                    next.putExtra("uri", uriString);

                    startActivity(next);
                } else{
                    Toast.makeText(ImageSelectActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openImages(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_IMAGE){
            if(resultCode == RESULT_OK){
                Uri selectImage = data.getData();
                InputStream inputStream = null;
                try{
                    assert selectImage !=null;
                    inputStream = getContentResolver().openInputStream(selectImage);
                    uriString = selectImage.toString();
                    IMAGE_SELECTED = true;
                } catch(FileNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(this, "Image could not be opened", Toast.LENGTH_SHORT).show();
                }
                BitmapFactory.decodeStream(inputStream);
                ivPreview.setImageURI(selectImage);
            }
        }
    }
}