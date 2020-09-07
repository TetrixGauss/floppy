package com.ath.floppy;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

public class UserActivity extends AppCompatActivity {

    ShapeableImageView userImage;

    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userImage = findViewById(R.id.user_header);
        float radius = getResources().getDimension(R.dimen.default_corner_radius);
        userImage.setShapeAppearanceModel(userImage.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED,radius)
                .setBottomLeftCorner(CornerFamily.ROUNDED,radius)
                .build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void goBack (View v){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}